package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.pgn.impl.tokens.CaptureToken;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A pawn can only capture pieces in forward diagonals. If the capture happens
 * on the last row, then it must promote. A capture "en passant" is a special
 * capture pattern, applicable to pawns which have moved forward by two squares,
 * from their starting position.
 * </p>
 * 
 * <p>
 * A pawn capture is written like "exd4", with the start column always being
 * specified. The origin point is therefore located on the disambiguated column,
 * one row back.
 * </p>
 */
public class PawnCaptureMove extends AbstractPawnMove {

    /**
     * Because of the "en passant" mechanics and our chosen implementation
     * algorithm, the captured vertex is found in the
     * {@link #ensureTargetAvailable(Color, Position)}, and cleared in the
     * {@link #tryEnPassantCapture(Color, Position)}. We therefore need to hold
     * a reference to it, which we put into a ThreadLocal, as we want to keep
     * the {@link #apply(Color, Position)} thread-safe.
     */
    private static ThreadLocal<Vertex> capturedSquare = new ThreadLocal<>();

    @Override
    public void apply(Color color, Position position) throws IllegalMoveException {
        super.apply(color, position);
        tryEnPassantCapture(position);
    }

    /**
     * A pawn capture should always have a disambiguation, even if there is only
     * one possible move.
     */
    @Override
    public boolean canApply(Color color, Position position) {
        return super.canApply(color, position) && getDisambiguation() != null;
    }

    @Override
    protected List<Vertex> getPossibleSources(Color color, Position position) {
        List<Vertex> candidates = new ArrayList<>();
        Vertex target = getTarget();
        Direction[] sideDirs = new Direction[] { Direction.EAST, Direction.WEST };
        Direction backDir = getBackDirection(color);
        for (Direction sideDir : sideDirs) {
            Vertex candidate = target.go(backDir).go(sideDir);
            if (candidate.isValid() && Piece.compare(position.getPiece(candidate), getFigurine(), color)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    /**
     * A pawn capture has two variants: the regular variant, where the target is
     * filled with a non-king enemy piece, and the "en passant" variant, when
     * the capture happens from a pawn starting position and the target is
     * empty.
     */
    @Override
    protected boolean ensureTargetAvailable(Color color, Position position) {
        capturedSquare.set(null);
        Vertex target = getTarget();
        if (target != null) {
            Piece captured = position.getPiece(target);
            if (captured != null) {
                return captured.getColor().equals(color.invert()) && !captured.getFigurine().equals(Figurine.KING);
            } else if (target.onThirdRow(color.invert())) {
                Vertex capture = target.go(getBackDirection(color));
                captured = position.getPiece(capture);
                if (captured != null && captured.getColor().equals(color.invert()) && captured.getFigurine().equals(Figurine.PAWN)) {
                    capturedSquare.set(capture);
                    return true;
                }
            }
        }
        return false;
    }

    private void tryEnPassantCapture(Position position) {
        Vertex capture = capturedSquare.get();
        if (capture != null) {
            position.setPiece(capture, null);
        }
    }

    @Override
    public String getUncheckedValue() {
        String value = super.getUncheckedValue();
        Vertex target = getTarget();
        Vertex disambiguation = getDisambiguation();
        if (target != null && disambiguation != null) {
            StringBuilder valueBuilder = new StringBuilder();
            valueBuilder.append(disambiguation.getCol());
            valueBuilder.append(CaptureToken.CAPTURE);
            valueBuilder.append(target.toString());
            valueBuilder.append(getPromotionValue());
            return valueBuilder.toString();
        }
        return value;
    }

    @Override
    public void cleanDisambiguation(Color color, Position position) {
        Vertex disambiguation = getDisambiguation();
        if (disambiguation != null) {
            setDisambiguation(new Vertex(disambiguation.getCol(), 0));
        }
    }
}
