package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;

/**
 * A queen moves in any direction (like a combination of a rook and a bishop).
 */
public class QueenMove extends AbstractFigurineDirectionalMove {

    @SuppressWarnings("javadoc")
    public QueenMove() {
        super(Figurine.QUEEN);
    }

    @Override
    protected Direction[] getDirections() {
        return Direction.values();
    }
}
