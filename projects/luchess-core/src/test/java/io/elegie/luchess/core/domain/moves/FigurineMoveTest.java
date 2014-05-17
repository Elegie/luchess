package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests applicable to all figurine moves. Isolated in that class so that they
 * are run only once.
 */
@SuppressWarnings("javadoc")
public class FigurineMoveTest {

    private static final Color PLAYER_COLOR = Color.WHITE;
    private static final Vertex SOURCE_VERTEX = new Vertex('e', 4);
    private static final Vertex TARGET_VERTEX = new Vertex('d', 5);
    private static final Vertex INVALID_VERTEX = new Vertex('z', 9);

    private AbstractFigurineMove move;
    private Position position;

    @Before
    public void setUp() {
        move = new BishopMove();
        move.setTarget(TARGET_VERTEX);

        position = new Position();
        position.setPiece(SOURCE_VERTEX, new Piece(Figurine.BISHOP, PLAYER_COLOR));
    }

    // --- Test Source --------------------------------------------------------

    @Test(expected = IllegalMoveException.class)
    public void testNoSource() throws IllegalMoveException {
        position.clear();
        move.apply(PLAYER_COLOR, position);
    }

    @Test(expected = IllegalMoveException.class)
    public void testWrongFigurineFound() throws IllegalMoveException {
        position.setPiece(SOURCE_VERTEX, new Piece(Figurine.KNIGHT, PLAYER_COLOR));
        move.apply(PLAYER_COLOR, position);
    }

    @Test(expected = IllegalMoveException.class)
    public void testWrongColorFound() throws IllegalMoveException {
        position.setPiece(SOURCE_VERTEX, new Piece(Figurine.BISHOP, PLAYER_COLOR.invert()));
        move.apply(PLAYER_COLOR, position);
    }

    // --- Test Disambiguation ------------------------------------------------

    /**
     * @throws IllegalMoveException
     *             Because the row of the disambiguation does not match.
     */
    @Test(expected = IllegalMoveException.class)
    public void testInconsistentDisambiguationRow() throws IllegalMoveException {
        testInconsistentDisambiguation(Direction.NORTH);
    }

    /**
     * @throws IllegalMoveException
     *             Because the column of the disambiguation does not match.
     */
    @Test(expected = IllegalMoveException.class)
    public void testInconsistentDisambiguationCol() throws IllegalMoveException {
        testInconsistentDisambiguation(Direction.WEST);
    }

    private void testInconsistentDisambiguation(Direction direction) throws IllegalMoveException {
        move.setDisambiguation(SOURCE_VERTEX.go(direction));
        move.apply(PLAYER_COLOR, position);
    }

    // --- Test Target --------------------------------------------------------

    /**
     * @throws IllegalMoveException
     *             Because the target has been omitted.
     */
    @Test(expected = IllegalMoveException.class)
    public void testMissingTarget() throws IllegalMoveException {
        move.setTarget(null);
        move.apply(PLAYER_COLOR, position);
    }

    /**
     * @throws IllegalMoveException
     *             Because the target is not valid.
     */
    @Test(expected = IllegalMoveException.class)
    public void testInvalidTarget() throws IllegalMoveException {
        move.setTarget(INVALID_VERTEX);
        move.apply(PLAYER_COLOR, position);
    }

    /**
     * Cannot move to a target occupied by a friendly piece.
     */
    @Test
    public void testTargetContainsFriend() {
        List<Figurine> friends = Arrays.asList(Figurine.BISHOP, Figurine.PAWN, Figurine.KNIGHT);
        for (Figurine friend : friends) {
            position.clear();
            position.setPiece(SOURCE_VERTEX, new Piece(Figurine.BISHOP, PLAYER_COLOR));
            position.setPiece(TARGET_VERTEX, new Piece(friend, PLAYER_COLOR));
            try {
                move.apply(PLAYER_COLOR, position);
                fail("IllegalMoveException expected");
            } catch (IllegalMoveException e) {
                assertTrue("Proper exception has been thrown", true);
            }
        }
    }

    // --- Test Value ---------------------------------------------------------

    @Test
    public void testValueTarget() {
        assertEquals("B" + TARGET_VERTEX.toString(), move.getValue());
    }

    @Test
    public void testValueInvalidTarget() {
        move.setTarget(INVALID_VERTEX);
        assertEquals("B", move.getValue());
    }

    @Test
    public void testValueMissingTarget() {
        move.setTarget(null);
        assertEquals("B", move.getValue());
    }

    @Test
    public void testValueTargetDisambiguation() {
        move.setDisambiguation(SOURCE_VERTEX);
        assertEquals("B" + SOURCE_VERTEX.toString() + TARGET_VERTEX.toString(), move.getValue());
    }

    @Test
    public void testValueTargetInvalidDisambiguation() {
        move.setDisambiguation(INVALID_VERTEX);
        assertEquals("B" + TARGET_VERTEX.toString(), move.getValue());
    }

    @Test
    public void testValueTargetDisambiguationCapture() {
        move.setDisambiguation(SOURCE_VERTEX);
        move.setCaptures(true);
        assertEquals("B" + SOURCE_VERTEX.toString() + "x" + TARGET_VERTEX.toString(), move.getValue());
    }

}
