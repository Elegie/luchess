package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.NORTH_EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH_WEST;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH_EAST;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH_WEST;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;

/**
 * A bishop moves along diagonals.
 */
public class BishopMove extends AbstractFigurineDirectionalMove {

    @SuppressWarnings("javadoc")
    public BishopMove() {
        super(Figurine.BISHOP);
    }

    @Override
    protected Direction[] getDirections() {
        return new Direction[] { NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST };
    }

}
