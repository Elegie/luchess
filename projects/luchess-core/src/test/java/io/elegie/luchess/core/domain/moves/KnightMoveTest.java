package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH;
import static io.elegie.luchess.core.domain.entities.Direction.WEST;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.LinkedList;
import java.util.List;

/**
 * A knight moves two moves in a given direction, then one move in a direction
 * perpendicular to the initial direction.
 */
public class KnightMoveTest extends AbstractFigurineMoveTest {

    @Override
    protected Class<? extends AbstractFigurineMove> getFigurineMove() {
        return KnightMove.class;
    }

    @Override
    protected List<Vertex> getValidTargets() {
        Direction[] first = new Direction[] { NORTH, NORTH, EAST, EAST, SOUTH, SOUTH, WEST, WEST };
        Direction[] second = new Direction[] { EAST, WEST, NORTH, SOUTH, EAST, WEST, NORTH, SOUTH };
        List<Vertex> validTargets = new LinkedList<>();
        for (int ii = 0; ii < first.length; ii++) {
            validTargets.add(SOURCE_VERTEX.go(first[ii]).go(first[ii]).go(second[ii]));
        }
        return validTargets;
    }
}
