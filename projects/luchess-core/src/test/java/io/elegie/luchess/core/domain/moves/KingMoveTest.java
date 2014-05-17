package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * A king can move in any direction, but only by one square.
 */
public class KingMoveTest extends AbstractFigurineMoveTest {

    @Override
    protected Class<? extends AbstractFigurineMove> getFigurineMove() {
        return KingMove.class;
    }

    @Override
    protected List<Vertex> getValidTargets() {
        List<Vertex> validTargets = new LinkedList<>();
        for (Direction direction : Direction.values()) {
            validTargets.add(SOURCE_VERTEX.go(direction));
        }
        return validTargets;
    }

    /**
     * @throws IllegalMoveException
     *             Because a king can only move one square away.
     */
    @Test(expected = IllegalMoveException.class)
    public void testTwoSquaresAway() throws IllegalMoveException {
        KingMove move = new KingMove();
        move.setTarget(SOURCE_VERTEX.go(Direction.NORTH).go(Direction.NORTH));

        Position position = new Position();
        position.setPiece(SOURCE_VERTEX, new Piece(Figurine.KING, PLAYER_COLOR));

        move.apply(PLAYER_COLOR, position);
    }

}
