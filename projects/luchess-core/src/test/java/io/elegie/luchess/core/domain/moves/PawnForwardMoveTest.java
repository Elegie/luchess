package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
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
 * Test pawn forward move (no capture).
 */
@SuppressWarnings("javadoc")
public class PawnForwardMoveTest {

    private static final Color PLAYER_COLOR = Color.WHITE;
    private static final Direction FORWARD = Direction.NORTH;
    private static final Piece PLAYER_PAWN = new Piece(Figurine.PAWN, PLAYER_COLOR);
    private static final Piece OPPONENT_PAWN = new Piece(Figurine.PAWN, PLAYER_COLOR.invert());
    private static final Piece PLAYER_KNIGHT = new Piece(Figurine.KNIGHT, PLAYER_COLOR);
    private static final Vertex SOURCE_VERTEX = new Vertex('e', 2);
    private static final Vertex TARGET_BEFORE_PROMOTE_VERTEX = new Vertex('e', 7);

    private PawnForwardMove move;
    private Position position;

    @Before
    public void setUp() {
        move = new PawnForwardMove();
        position = new Position();
    }

    // --- Test forward 1 move ------------------------------------------------

    @Test
    public void testForward1M() throws IllegalMoveException {
        testForward1MFromSourceWith(PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward1MMissingPiece() throws IllegalMoveException {
        testForward1MFromSourceWith(null);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward1MWrongPiece() throws IllegalMoveException {
        testForward1MFromSourceWith(PLAYER_KNIGHT);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward1MWrongColor() throws IllegalMoveException {
        testForward1MFromSourceWith(OPPONENT_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward1MAlreadyOccupied() throws IllegalMoveException {
        position.setPiece(SOURCE_VERTEX.go(FORWARD), OPPONENT_PAWN);
        testForward1MFromSourceWith(PLAYER_PAWN);
    }

    // --- Test forward 2 moves -----------------------------------------------

    @Test
    public void testForward2M() throws IllegalMoveException {
        testForward2MFromWith(SOURCE_VERTEX, PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward2MNotOnStartRow() throws IllegalMoveException {
        testForward2MFromWith(SOURCE_VERTEX.go(FORWARD), PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForward2MObstacle() throws IllegalMoveException {
        position.setPiece(SOURCE_VERTEX.go(FORWARD), OPPONENT_PAWN);
        testForward2MFromWith(SOURCE_VERTEX, PLAYER_PAWN);
    }

    // --- Test promotion -----------------------------------------------------

    @Test
    public void testForwardPromotion() throws IllegalMoveException {
        move.setPromotion(Figurine.QUEEN);
        testForward1MFromWith(TARGET_BEFORE_PROMOTE_VERTEX, PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForwardPromotionMissing() throws IllegalMoveException {
        testForward1MFromWith(TARGET_BEFORE_PROMOTE_VERTEX, PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForwardPromotionWrongRow() throws IllegalMoveException {
        move.setPromotion(Figurine.QUEEN);
        testForward1MFromWith(SOURCE_VERTEX, PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForwardPromotionPawn() throws IllegalMoveException {
        move.setPromotion(Figurine.PAWN);
        testForward1MFromWith(TARGET_BEFORE_PROMOTE_VERTEX, PLAYER_PAWN);
    }

    @Test(expected = IllegalMoveException.class)
    public void testForwardPromotionKing() throws IllegalMoveException {
        move.setPromotion(Figurine.KING);
        testForward1MFromWith(TARGET_BEFORE_PROMOTE_VERTEX, PLAYER_PAWN);
    }

    // --- Missing target -----------------------------------------------------

    @Test(expected = IllegalMoveException.class)
    public void testForwardNoTarget() throws IllegalMoveException {
        move.apply(PLAYER_COLOR, position);
    }

    // --- Test disambiguation cleaning ---------------------------------------

    @Test
    public void testDisambiguationCleaning() {
        move.setDisambiguation(SOURCE_VERTEX);
        assertEquals(SOURCE_VERTEX, move.getDisambiguation());
        move.cleanDisambiguation(PLAYER_COLOR, position);
        assertEquals(null, move.getDisambiguation());
    }

    // --- Test value ---------------------------------------------------------

    @Test
    public void testValues() {
        move.setTarget(SOURCE_VERTEX);
        assertEquals(SOURCE_VERTEX.toString(), move.getValue());

        move.setPromotion(Figurine.QUEEN);
        assertEquals(SOURCE_VERTEX.toString() + "=" + Figurine.QUEEN.toString(), move.getValue());
    }

    // --- Helpers ------------------------------------------------------------

    private void advance(Vertex target) throws IllegalMoveException {
        Figurine figurine = move.getPromotion();
        if (figurine == null) {
            figurine = move.getFigurine();
        }
        move.setTarget(target);
        move.apply(PLAYER_COLOR, position);
        PositionHelper.validateEndPosition(position, target, new Piece(figurine, PLAYER_COLOR));
    }

    private void testForward1MFromSourceWith(Piece piece) throws IllegalMoveException {
        testForward1MFromWith(SOURCE_VERTEX, piece);
    }

    private void testForward1MFromWith(Vertex source, Piece piece) throws IllegalMoveException {
        position.setPiece(source, piece);
        advance(source.go(FORWARD));
    }

    private void testForward2MFromWith(Vertex source, Piece piece) throws IllegalMoveException {
        position.setPiece(source, piece);
        advance(source.go(FORWARD).go(FORWARD));
    }

}
