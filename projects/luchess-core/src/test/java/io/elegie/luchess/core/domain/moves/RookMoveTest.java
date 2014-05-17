package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH;
import static io.elegie.luchess.core.domain.entities.Direction.WEST;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.Arrays;
import java.util.List;

/**
 * A rook moves horizontally or vertically.
 */
public class RookMoveTest extends AbstractFigurineMoveTest {

    @Override
    protected Class<? extends AbstractFigurineMove> getFigurineMove() {
        return RookMove.class;
    }

    @Override
    protected List<Vertex> getValidTargets() {
        Vertex N = SOURCE_VERTEX.go(NORTH);
        Vertex W = SOURCE_VERTEX.go(WEST).go(WEST);
        Vertex S = SOURCE_VERTEX.go(SOUTH).go(SOUTH).go(SOUTH);
        Vertex E = SOURCE_VERTEX.go(EAST).go(EAST).go(EAST);
        return Arrays.asList(N, W, S, E);
    }

}
