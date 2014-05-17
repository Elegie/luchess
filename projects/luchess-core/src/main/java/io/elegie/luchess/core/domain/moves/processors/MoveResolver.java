package io.elegie.luchess.core.domain.moves.processors;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.moves.AbstractFigurineMove;
import io.elegie.luchess.core.domain.moves.AbstractMove;
import io.elegie.luchess.core.domain.moves.AbstractPawnMove;
import io.elegie.luchess.core.domain.moves.BishopMove;
import io.elegie.luchess.core.domain.moves.CastleMove;
import io.elegie.luchess.core.domain.moves.KingMove;
import io.elegie.luchess.core.domain.moves.KnightMove;
import io.elegie.luchess.core.domain.moves.PawnCaptureMove;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;
import io.elegie.luchess.core.domain.moves.QueenMove;
import io.elegie.luchess.core.domain.moves.RookMove;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A move resolver is used to find and build a move object, given a particular
 * position, a starting vertex, an ending vertex, and an optional promotion
 * figurine (for pawn moves).
 */
@SuppressWarnings("javadoc")
public class MoveResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MoveResolver.class);

    private Vertex disambiguation;
    private Vertex target;
    private Position position;
    private Figurine promotedFigurine;

    /**
     * @return The move built from the position and provided vertices /
     *         promotion figurine - or null if no move could be built from the
     *         supplied data.
     */
    public AbstractMove resolve() {
        if (checkInputs()) {
            Color color = findColor();
            for (AbstractMove move : getAllMoves()) {
                move.setDisambiguation(disambiguation);
                move.setTarget(target);
                move.setCaptures(position.getPiece(target) != null);
                if (promotedFigurine != null && (move instanceof AbstractPawnMove)) {
                    AbstractPawnMove pawnMove = (AbstractPawnMove) move;
                    pawnMove.setPromotion(promotedFigurine);
                }
                if (move.canApply(color, position)) {
                    if (move instanceof AbstractFigurineMove) {
                        AbstractFigurineMove figurineMove = (AbstractFigurineMove) move;
                        try {
                            figurineMove.cleanDisambiguation(color, position);
                        } catch (IllegalMoveException e) {
                            LOG.warn(e.getMessage(), e);
                            return null;
                        }
                    }
                    return move;
                }
            }
        }
        return null;
    }

    private boolean checkInputs() {
        if (position == null) {
            return false;
        }
        if (target == null || !target.isValid()) {
            return false;
        }
        if (disambiguation == null || !disambiguation.isValid() || position.getPiece(disambiguation) == null) {
            return false;
        }
        return true;
    }

    private List<AbstractMove> getAllMoves() {
        List<AbstractMove> allMoves = new ArrayList<>();
        allMoves.add(new BishopMove());
        allMoves.add(new CastleMove(false));
        allMoves.add(new CastleMove(true));
        allMoves.add(new KingMove());
        allMoves.add(new KnightMove());
        allMoves.add(new PawnCaptureMove());
        allMoves.add(new PawnForwardMove());
        allMoves.add(new QueenMove());
        allMoves.add(new RookMove());
        return allMoves;
    }

    private Color findColor() {
        return position.getPiece(disambiguation).getColor();
    }

    // --- Getters / Setters --------------------------------------------------

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {disambiguation: %s, target: %s, promotedFigurine: %s, position: %s}";
        return String.format(value, className, disambiguation, target, promotedFigurine, position);
    }

    public Vertex getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(Vertex disambiguation) {
        this.disambiguation = disambiguation;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Figurine getPromotedFigurine() {
        return promotedFigurine;
    }

    public void setPromotedFigurine(Figurine promotedFigurine) {
        this.promotedFigurine = promotedFigurine;
    }

}
