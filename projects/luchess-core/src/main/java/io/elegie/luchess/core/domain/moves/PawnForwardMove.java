package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * A pawn can only move forward, by one square, except when it moves from its
 * start position, in which case it may move forward by two squares. When
 * reaching the last row, the pawn must promote into another piece.
 */
public class PawnForwardMove extends AbstractPawnMove {

    @Override
    protected List<Vertex> getPossibleSources(Color color, Position position) {
        List<Vertex> candidates = new ArrayList<>();
        Direction backDir = getBackDirection(color);
        Vertex target = getTarget();
        Vertex candidate = target.go(backDir);
        if (candidate.isValid()) {
            Piece piece = position.getPiece(candidate);
            if (piece == null && target.onFourthRow(color)) {
                candidate = candidate.go(backDir);
                if (candidate.isValid()) {
                    piece = position.getPiece(candidate);
                }
            }
            if (Piece.compare(piece, getFigurine(), color)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    /**
     * A pawn forward move excludes a capture by definition, so the target
     * vertex must be empty.
     */
    @Override
    protected boolean ensureTargetAvailable(Color color, Position position) {
        Vertex target = getTarget();
        return target != null && position.getPiece(target) == null;
    }

    @Override
    public String getUncheckedValue() {
        String value = super.getUncheckedValue();
        Vertex target = getTarget();
        if (target != null) {
            value = target.toString() + getPromotionValue();
        }
        return value;
    }

    @Override
    public void cleanDisambiguation(Color color, Position position) {
        setDisambiguation(null);
    }

}
