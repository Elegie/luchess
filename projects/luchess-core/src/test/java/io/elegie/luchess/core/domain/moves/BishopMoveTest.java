package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.NORTH_EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH_WEST;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH_EAST;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH_WEST;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.Arrays;
import java.util.List;

/**
 * A bishop moves on diagonals.
 */
public class BishopMoveTest extends AbstractFigurineMoveTest {

    @Override
    protected Class<? extends AbstractFigurineMove> getFigurineMove() {
        return BishopMove.class;
    }

    @Override
    protected List<Vertex> getValidTargets() {
        Vertex NW = SOURCE_VERTEX.go(NORTH_WEST);
        Vertex SW = SOURCE_VERTEX.go(SOUTH_WEST).go(SOUTH_WEST);
        Vertex SE = SOURCE_VERTEX.go(SOUTH_EAST).go(SOUTH_EAST).go(SOUTH_EAST);
        Vertex NE = SOURCE_VERTEX.go(NORTH_EAST).go(NORTH_EAST).go(NORTH_EAST);
        return Arrays.asList(NW, SW, SE, NE);
    }

}
