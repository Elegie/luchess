package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.helpers.PositionHelper;

import org.junit.Before;
import org.junit.Test;

/**
 * Test pawn captures. Promotion testing is tested in
 * {@link PawnForwardMoveTest#testForwardPromotion()} and the likes.
 */
@SuppressWarnings("javadoc")
public class PawnCaptureMoveTest {

    private static final Color PLAYER_COLOR = Color.WHITE;
    private static final Direction BACKWARD = Direction.SOUTH;
    private static final Piece PLAYER_PAWN = new Piece(Figurine.PAWN, PLAYER_COLOR);
    private static final Piece OPPONENT_PAWN = new Piece(Figurine.PAWN, PLAYER_COLOR.invert());
    private static final Piece OPPONENT_KING = new Piece(Figurine.KING, PLAYER_COLOR.invert());
    private static final Piece OPPONENT_KNIGHT = new Piece(Figurine.KNIGHT, PLAYER_COLOR.invert());
    private static final Vertex SOURCE_VERTEX = new Vertex('e', 5);
    private static final Vertex TARGET_VERTEX = new Vertex('d', 6);

    private PawnCaptureMove move;
    private Position position;

    @Before
    public void setUp() {
        move = new PawnCaptureMove();
        position = new Position();
    }

    // --- Test normal capture ------------------------------------------------

    @Test
    public void testCapture() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX, OPPONENT_PAWN);
        capture();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureMissingDisambiguation() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX, OPPONENT_PAWN);
        captureNoDisambiguation();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureFriendAtTarget() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX, PLAYER_PAWN);
        capture();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureKingAtTarget() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX, OPPONENT_KING);
        capture();
    }

    // --- Test capture en passant --------------------------------------------

    @Test
    public void testCaptureEnPassant() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX.go(BACKWARD), OPPONENT_PAWN);
        capture();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureEnPassantMissingTarget() throws IllegalMoveException {
        capture();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureEnPassantFriendAtTarget() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX.go(BACKWARD), PLAYER_PAWN);
        capture();
    }

    @Test(expected = IllegalMoveException.class)
    public void testCaptureEnPassantNotAPawnAtTarget() throws IllegalMoveException {
        position.setPiece(TARGET_VERTEX.go(BACKWARD), OPPONENT_KNIGHT);
        capture();
    }

    // --- Missing target -----------------------------------------------------

    @Test(expected = IllegalMoveException.class)
    public void testCaptureNoTarget() throws IllegalMoveException {
        move.apply(PLAYER_COLOR, position);
    }

    // --- Test values --------------------------------------------------------

    @Test
    public void testValues() {
        move.setDisambiguation(SOURCE_VERTEX);
        move.setTarget(TARGET_VERTEX);
        move.setCaptures(true);

        String firstPart = Character.toString(SOURCE_VERTEX.getCol()) + "x" + TARGET_VERTEX.toString();
        assertEquals(firstPart, move.getValue());

        move.setPromotion(Figurine.QUEEN);
        assertEquals(firstPart + "=" + Figurine.QUEEN.toString(), move.getValue());
    }

    // --- Test disambiguation cleaning ---------------------------------------

    @Test
    public void testDisambiguationCleaning() {
        move.setDisambiguation(null);
        move.cleanDisambiguation(PLAYER_COLOR, position);
        assertEquals(null, move.getDisambiguation());
        move.setDisambiguation(SOURCE_VERTEX);
        assertTrue(move.getDisambiguation().isValid());
        move.cleanDisambiguation(PLAYER_COLOR, position);
        assertFalse(move.getDisambiguation().isValid());
    }

    // --- Helpers ------------------------------------------------------------

    private void capture() throws IllegalMoveException {
        move.setDisambiguation(SOURCE_VERTEX);
        captureNoDisambiguation();
    }

    private void captureNoDisambiguation() throws IllegalMoveException {
        position.setPiece(SOURCE_VERTEX, PLAYER_PAWN);
        move.setTarget(TARGET_VERTEX);
        move.apply(PLAYER_COLOR, position);
        PositionHelper.validateEndPosition(position, TARGET_VERTEX, PLAYER_PAWN);
    }
}
