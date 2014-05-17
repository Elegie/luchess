package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.pgn.impl.tokens.CaptureToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A figurine move is a move made by a figurine. The figurine should be passed
 * to the constructor.
 */
public abstract class AbstractFigurineMove extends AbstractMove {

    /**
     * Since we separate the logic between {@link #canApply(Color, Position)}
     * and {@link #apply(Color, Position)}, we want to cache the value of the
     * source already calculated in the first method call (canApply), in order
     * to not calculate it again in the second method call (apply).
     */
    private static ThreadLocal<Vertex> calculatedSource = new ThreadLocal<>();

    private Figurine figurine;

    /**
     * @param figurine
     *            The figurine of the piece being moved.
     */
    public AbstractFigurineMove(Figurine figurine) {
        this.figurine = figurine;
    }

    @SuppressWarnings("javadoc")
    public Figurine getFigurine() {
        return figurine;
    }

    /**
     * Implementations are supposed to calculate and return all possible source
     * vertices for the move, using the rules for the implied figurine, with the
     * given position and known move properties.
     * 
     * @return A list of vertices to be disambiguated from.
     * @throws IllegalMoveException
     *             When no source vertex could be found, given all provided
     *             parameters.
     */
    protected abstract List<Vertex> getPossibleSources(Color color, Position position);

    /**
     * Constructs a value for the move, appending figurine, disambiguated
     * coordinates if any, capture if any, and target coordinates.
     */
    @Override
    public String getUncheckedValue() {
        StringBuilder value = new StringBuilder(getFigurine().toString());
        Vertex disambiguation = getDisambiguation();
        if (disambiguation != null) {
            if (disambiguation.isValidCol()) {
                value.append(disambiguation.getCol());
            }
            if (disambiguation.isValidRow()) {
                value.append(disambiguation.getRow());
            }
        }
        if (getCaptures()) {
            value.append(CaptureToken.CAPTURE);
        }
        Vertex target = getTarget();
        if (target != null) {
            if (target.isValidCol()) {
                value.append(target.getCol());
            }
            if (target.isValidRow()) {
                value.append(target.getRow());
            }
        }
        return value.toString();
    }

    /**
     * We use a generic algorithm to apply the move onto a given position.
     * First, we make sure that the target location of the move is available,
     * then we execute the move, removing the piece from the original location,
     * and putting it onto the target location.
     */
    @Override
    public void apply(Color color, Position position) throws IllegalMoveException {
        if (canApply(color, position)) {
            position.setPiece(calculatedSource.get(), null);
            position.setPiece(getTarget(), new Piece(getFigurine(), color));
        } else {
            String message = "Cannot apply move %s for color %s onto following position: %s";
            message = String.format(message, this, color, position);
            throw new IllegalMoveException(message);
        }
    }

    @Override
    public boolean canApply(Color color, Position position) {
        calculatedSource.set(null);
        boolean targetAvailable = ensureTargetAvailable(color, position);
        if (targetAvailable) {
            Vertex source = disambiguateSourceCandidates(getPossibleSources(color, position));
            if (source != null) {
                calculatedSource.set(source);
                return true;
            }
        }
        return false;
    }

    /**
     * Given a position and color, update the set disambiguation of the move,
     * keeping only minimal information. Trimming the disambiguation is useful
     * because it used to generate the string representation of the move.
     * 
     * @param color
     *            Color of the move being played.
     * @param position
     *            Position upon which the move is being played.
     * @throws IllegalMoveException
     *             When the disambiguation data are illegal with this position.
     */
    public void cleanDisambiguation(Color color, Position position) throws IllegalMoveException {
        if (getTarget() != null && getDisambiguation() != null) {
            List<Vertex> possibleSources = getPossibleSources(color, position);
            if (possibleSources.isEmpty() || possibleSources.size() == 1) {
                setDisambiguation(null);
            } else {
                removeNotDiscriminatingColRow(possibleSources);
            }
        }
    }

    /**
     * Remove useless column/row disambiguations. Column disambiguation takes
     * priority over row disambiguation. At method call, the possible sources
     * are guaranteed to always be more than one.
     * 
     * @param possibleSources
     * @throws IllegalMoveException
     */
    private void removeNotDiscriminatingColRow(List<Vertex> possibleSources) throws IllegalMoveException {
        Vertex disambiguation = getDisambiguation();
        Set<Vertex> matchedCols = getMatchedCols(disambiguation, possibleSources);
        Set<Vertex> matchedRows = getMatchedRows(disambiguation, possibleSources);
        if (!disambiguation.isValidCol() && !disambiguation.isValidRow()) {
            String message = "Many sources, yet no valid disambiguation.";
            throw new IllegalMoveException(message);
        }
        if (!disambiguation.isValidCol()) {
            if (matchedRows.size() != 1) {
                String message = "Col disambiguation irrelevant, too many possible disambiguated rows.";
                throw new IllegalMoveException(message);
            }
            setDisambiguation(matchedRows.iterator().next());
            removeNotDiscriminatingColRow(possibleSources);
        } else if (!disambiguation.isValidRow()) {
            if (matchedCols.size() != 1) {
                String message = "Row disambiguation irrelevant, too many possible disambiguated cols.";
                throw new IllegalMoveException(message);
            }
        } else {
            if (matchedCols.size() == 1) {
                setDisambiguation(new Vertex(disambiguation.getCol(), 0));
            } else if (matchedRows.size() == 1) {
                setDisambiguation(new Vertex((char) 0, disambiguation.getRow()));
            } else {
                Set<Vertex> intersection = new HashSet<>(matchedCols);
                intersection.retainAll(matchedRows);
                if (intersection.size() != 1) {
                    String message = "Both disambiguations relevant, yet intersection does not yield a clear disambiguation.";
                    throw new IllegalMoveException(message);
                }
            }
        }
    }

    private Set<Vertex> getMatchedCols(Vertex disambiguation, List<Vertex> possibleSources) {
        Set<Vertex> matchedCols = new HashSet<>();
        if (disambiguation.isValidCol()) {
            char col = disambiguation.getCol();
            for (Vertex possibleSource : possibleSources) {
                if (possibleSource.getCol() == col) {
                    matchedCols.add(possibleSource);
                }
            }
        }
        return matchedCols;
    }

    private Set<Vertex> getMatchedRows(Vertex disambiguation, List<Vertex> possibleSources) {
        Set<Vertex> matchedRows = new HashSet<>();
        if (disambiguation.isValidRow()) {
            int row = disambiguation.getRow();
            for (Vertex possibleSource : possibleSources) {
                if (possibleSource.getRow() == row) {
                    matchedRows.add(possibleSource);
                }
            }
        }
        return matchedRows;
    }

    /**
     * Makes sure that the move can be made onto the target location, i.e the
     * target should be either empty, or occupied by an non-king enemy piece
     * (pending capture).
     * 
     * @throws IllegalMoveException
     *             When the move cannot be made.
     */
    protected boolean ensureTargetAvailable(Color color, Position position) {
        Vertex target = getTarget();
        if (target != null && target.isValid()) {
            Piece captured = position.getPiece(target);
            if (captured == null || (captured.getColor().equals(color.invert()) && !captured.getFigurine().equals(Figurine.KING))) {
                return true;
            }
        }
        return false;
    }

    /**
     * The method is a helper method, which can be used by subclasses to find
     * the valid source vertex from a list of possible candidates, using
     * disambiguation data. For instance, a knight move could be made by two
     * knights located on the board, so one should identify the right knight,
     * using disambiguated coordinates (N4e3 or Nfe3?).
     * 
     * @param candidates
     *            The possible vertices to choose from.
     * @return The resulting source vertex, or null if none was found.
     */
    private Vertex disambiguateSourceCandidates(List<Vertex> candidates) {
        for (Vertex candidate : candidates) {
            Vertex disambiguation = getDisambiguation();
            if (!candidateDisambiguated(candidate, disambiguation)) {
                continue;
            }
            return candidate;
        }
        return null;
    }

    private boolean candidateDisambiguated(Vertex candidate, Vertex disambiguation) {
        if (disambiguation != null) {
            if (disambiguation.isValidCol() && disambiguation.getCol() != candidate.getCol()) {
                return false;
            }
            if (disambiguation.isValidRow() && disambiguation.getRow() != candidate.getRow()) {
                return false;
            }
        }
        return true;
    }
}
