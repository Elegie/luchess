package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH;
import static io.elegie.luchess.core.domain.entities.Direction.WEST;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;

/**
 * A rook moves horizontally or vertically.
 */
public class RookMove extends AbstractFigurineDirectionalMove {

    @SuppressWarnings("javadoc")
    public RookMove() {
        super(Figurine.ROOK);
    }

    @Override
    protected Direction[] getDirections() {
        return new Direction[] { NORTH, WEST, SOUTH, EAST };
    }

}
