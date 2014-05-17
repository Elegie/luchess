package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

import org.junit.Before;
import org.junit.Test;

/**
 * Checks that a castle is properly conducted. We do not take into account the
 * following rules: that the castle path is not checked by the enemy, or that
 * the castled pieces have not moved previously.
 */
@SuppressWarnings("javadoc")
public class CastleMoveTest {

    private static final Color PLAYER_COLOR = Color.WHITE;
    private static final Vertex KING_VERTEX = new Vertex(Vertex.KING_COL, Vertex.FIRST_WHITE_ROW);
    private static final Vertex KING_ROOK_VERTEX = new Vertex(Vertex.KING_ROOK_COL, Vertex.FIRST_WHITE_ROW);
    private static final Vertex QUEEN_ROOK_VERTEX = new Vertex(Vertex.QUEEN_ROOK_COL, Vertex.FIRST_WHITE_ROW);

    private static final Vertex END_KING_POS_SMALL = new Vertex(Vertex.KING_KNIGHT_COL, Vertex.FIRST_WHITE_ROW);
    private static final Vertex END_ROOK_POS_SMALL = new Vertex(Vertex.KING_BISHOP_COL, Vertex.FIRST_WHITE_ROW);
    private static final Vertex END_KING_POS_LARGE = new Vertex(Vertex.QUEEN_BISHOP_COL, Vertex.FIRST_WHITE_ROW);
    private static final Vertex END_ROOK_POS_LARGE = new Vertex(Vertex.QUEEN_COL, Vertex.FIRST_WHITE_ROW);

    private Position position;

    @Before
    public void setUp() {
        position = new Position();
        position.setPiece(KING_VERTEX, new Piece(Figurine.KING, PLAYER_COLOR));
        position.setPiece(KING_ROOK_VERTEX, new Piece(Figurine.ROOK, PLAYER_COLOR));
        position.setPiece(QUEEN_ROOK_VERTEX, new Piece(Figurine.ROOK, PLAYER_COLOR));
    }

    // --- Test start position ------------------------------------------------

    @Test(expected = IllegalMoveException.class)
    public void testKingPositionNotEmpty() throws IllegalMoveException {
        testPiecePositionNotEmpty(KING_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testKingPositionHasKing() throws IllegalMoveException {
        testPiecePositionHasExpectedPiece(KING_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testKingPositionHasWrongColor() throws IllegalMoveException {
        testPositionHasWrongColor(KING_VERTEX, Figurine.KING);
    }

    @Test(expected = IllegalMoveException.class)
    public void testKingRookPositionNotEmpty() throws IllegalMoveException {
        testPiecePositionNotEmpty(KING_ROOK_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testKingRookPositionHasRook() throws IllegalMoveException {
        testPiecePositionHasExpectedPiece(KING_ROOK_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testKingRookPositionHasWrongColor() throws IllegalMoveException {
        testPositionHasWrongColor(KING_ROOK_VERTEX, Figurine.ROOK);
    }

    @Test(expected = IllegalMoveException.class)
    public void testQueenRookPositionNotEmpty() throws IllegalMoveException {
        testPiecePositionNotEmpty(QUEEN_ROOK_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testQueenRookPositionHasRook() throws IllegalMoveException {
        testPiecePositionHasExpectedPiece(QUEEN_ROOK_VERTEX);
    }

    @Test(expected = IllegalMoveException.class)
    public void testQueenRookPositionHasWrongColor() throws IllegalMoveException {
        testPositionHasWrongColor(QUEEN_ROOK_VERTEX, Figurine.ROOK);
    }

    @Test(expected = IllegalMoveException.class)
    public void testWrongDisambiguation() throws IllegalMoveException {
        CastleMove move = new CastleMove(true);
        move.setDisambiguation(new Vertex('e', 4));
        move.apply(PLAYER_COLOR, position);
    }

    @Test(expected = IllegalMoveException.class)
    public void testWrongTarget() throws IllegalMoveException {
        CastleMove move = new CastleMove(true);
        move.setTarget(new Vertex('e', 4));
        move.apply(PLAYER_COLOR, position);
    }

    private void testPiecePositionNotEmpty(Vertex vertex) throws IllegalMoveException {
        boolean isSmall = !vertex.equals(QUEEN_ROOK_VERTEX);
        position.setPiece(vertex, null);
        castle(isSmall);
    }

    private void testPiecePositionHasExpectedPiece(Vertex vertex) throws IllegalMoveException {
        boolean isSmall = !vertex.equals(QUEEN_ROOK_VERTEX);
        position.setPiece(vertex, new Piece(Figurine.PAWN, PLAYER_COLOR));
        castle(isSmall);
    }

    private void testPositionHasWrongColor(Vertex vertex, Figurine figurine) throws IllegalMoveException {
        boolean isSmall = !vertex.equals(QUEEN_ROOK_VERTEX);
        position.setPiece(vertex, new Piece(figurine, PLAYER_COLOR.invert()));
        castle(isSmall);
    }

    // --- Test castle --------------------------------------------------------

    @Test
    public void testSmallCastle() throws IllegalMoveException {
        position.setPiece(QUEEN_ROOK_VERTEX, null);
        castle(true);
    }

    @Test
    public void testLargeCastle() throws IllegalMoveException {
        position.setPiece(KING_ROOK_VERTEX, null);
        castle(false);
    }

    @Test
    public void testCorrectDisambiguation() throws IllegalMoveException {
        CastleMove move = new CastleMove(true);
        move.setDisambiguation(KING_VERTEX);
        move.apply(PLAYER_COLOR, position);
    }

    @Test
    public void testCorrectTarget() throws IllegalMoveException {
        CastleMove move = new CastleMove(true);
        move.setTarget(END_KING_POS_SMALL);
        move.apply(PLAYER_COLOR, position);
    }

    @Test(expected = IllegalMoveException.class)
    public void testSmallCastleWithObstacle() throws IllegalMoveException {
        position.setPiece(END_ROOK_POS_SMALL, new Piece(Figurine.PAWN, PLAYER_COLOR));
        castle(true);
    }

    @Test(expected = IllegalMoveException.class)
    public void testLargeCastleWithObstacle() throws IllegalMoveException {
        position.setPiece(END_ROOK_POS_LARGE, new Piece(Figurine.PAWN, PLAYER_COLOR));
        castle(false);
    }

    // --- Test value ---------------------------------------------------------

    @Test
    public void testValues() {
        assertEquals("O-O", new CastleMove(true).getValue());
        assertEquals("O-O-O", new CastleMove(false).getValue());
    }

    // --- Helpers ------------------------------------------------------------

    private void castle(boolean isSmall) throws IllegalMoveException {
        new CastleMove(isSmall).apply(PLAYER_COLOR, position);
        if (isSmall) {
            validateEndPosition(END_KING_POS_SMALL, END_ROOK_POS_SMALL);
        } else {
            validateEndPosition(END_KING_POS_LARGE, END_ROOK_POS_LARGE);
        }
    }

    private void validateEndPosition(Vertex king, Vertex rook) {
        for (int row = Vertex.START_ROW; row <= Vertex.END_ROW; row++) {
            for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
                Vertex current = new Vertex(col, row);
                Piece piece = position.getPiece(current);
                if (current.equals(king)) {
                    assertEquals(PLAYER_COLOR, piece.getColor());
                    assertEquals(Figurine.KING, piece.getFigurine());
                } else if (current.equals(rook)) {
                    assertEquals(PLAYER_COLOR, piece.getColor());
                    assertEquals(Figurine.ROOK, piece.getFigurine());
                } else {
                    assertNull(piece);
                }
            }
        }
    }

}
