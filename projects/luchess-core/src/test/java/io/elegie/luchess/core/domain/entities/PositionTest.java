package io.elegie.luchess.core.domain.entities;

import static io.elegie.luchess.core.domain.entities.Color.BLACK;
import static io.elegie.luchess.core.domain.entities.Color.WHITE;
import static io.elegie.luchess.core.domain.entities.Figurine.BISHOP;
import static io.elegie.luchess.core.domain.entities.Figurine.KING;
import static io.elegie.luchess.core.domain.entities.Figurine.KNIGHT;
import static io.elegie.luchess.core.domain.entities.Figurine.PAWN;
import static io.elegie.luchess.core.domain.entities.Figurine.QUEEN;
import static io.elegie.luchess.core.domain.entities.Figurine.ROOK;
import static io.elegie.luchess.core.domain.entities.Vertex.END_COL;
import static io.elegie.luchess.core.domain.entities.Vertex.END_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.FIRST_BLACK_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.FIRST_WHITE_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.FOURTH_BLACK_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.FOURTH_WHITE_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.SECOND_BLACK_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.SECOND_WHITE_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.START_COL;
import static io.elegie.luchess.core.domain.entities.Vertex.THIRD_BLACK_ROW;
import static io.elegie.luchess.core.domain.entities.Vertex.THIRD_WHITE_ROW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.exceptions.InvalidPositionException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PositionTest {

    private Position position;

    @Before
    public void setUp() {
        position = new Position();
    }

    /**
     * Check that clearing the position leaves no piece on board.
     */
    @Test
    public void testPositionClear() {
        for (char col = START_COL; col <= END_COL; col++) {
            for (int row = Vertex.START_ROW; row <= END_ROW; row++) {
                Vertex vertex = new Vertex(col, row);
                assertNull(position.getPiece(vertex));
            }
        }
    }

    @Test
    public void testPositionPieceRetrieval() {
        Piece piece = new Piece(PAWN, WHITE);
        Vertex vertex = new Vertex('e', 4);
        assertNull(position.getPiece(vertex));
        position.setPiece(vertex, piece);
        assertEquals(piece, position.getPiece(vertex));
    }

    /**
     * Check that the starting position of the board is a valid starting chess
     * position.
     */
    @Test
    public void testPositionInit() {
        position.init();
        Figurine[] pawns = new Figurine[] { PAWN, PAWN, PAWN, PAWN, PAWN, PAWN, PAWN, PAWN };
        Figurine[] figurines = new Figurine[] { ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK };
        testRow(FIRST_WHITE_ROW, WHITE, figurines);
        testRow(FIRST_BLACK_ROW, BLACK, figurines);
        testRow(SECOND_WHITE_ROW, WHITE, pawns);
        testRow(SECOND_BLACK_ROW, BLACK, pawns);
        testRow(THIRD_WHITE_ROW, null, null);
        testRow(THIRD_BLACK_ROW, null, null);
        testRow(FOURTH_WHITE_ROW, null, null);
        testRow(FOURTH_BLACK_ROW, null, null);

    }

    private void testRow(int row, Color color, Figurine[] figurines) {
        for (int ii = 0; ii < END_ROW; ii++) {
            Vertex vertex = new Vertex((char) (START_COL + ii), row);
            Piece piece = null;
            if (color != null && figurines != null) {
                piece = new Piece(figurines[ii], color);
            }
            assertEquals(piece, position.getPiece(vertex));
        }
    }

    // --- Test serialization -------------------------------------------------

    @Test
    public void testSerialization() throws InvalidPositionException {
        Position startPos = new Position();
        startPos.init();
        assertEquals(startPos, Position.deserialize(Position.serialize(startPos)));
    }

    @Test(expected = InvalidPositionException.class)
    public void testEmptyDeserialization() throws InvalidPositionException {
        Position.deserialize(null);
    }

    @Test(expected = InvalidPositionException.class)
    public void testWrongBoardSize() throws InvalidPositionException {
        Position.deserialize(new Piece(Figurine.ROOK, Color.BLACK).toString());
    }

    // --- Test structure -----------------------------------------------------

    @Test
    public void testEqualsHashcode() {
        Position first = new Position();
        assertTrue(first.equals(first));
        assertFalse(first.equals("a string"));

        Position second = new Position();
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());

        second.init();
        first.setPiece(new Vertex('e', 4), new Piece(Figurine.QUEEN, Color.WHITE));
        first.setPiece(new Vertex('a', 1), new Piece(Figurine.ROOK, Color.WHITE));
        first.setPiece(new Vertex('b', 1), new Piece(Figurine.QUEEN, Color.WHITE));
        assertFalse(second.equals(first));
        assertFalse(first.hashCode() == second.hashCode());
    }

    @Test
    public void testToString() {
        position.init();
        String representation = "\n" +
                "8|BR BN BB BQ BK BB BN BR\n" +
                "7|BP BP BP BP BP BP BP BP\n" +
                "6|.. .. .. .. .. .. .. ..\n" +
                "5|.. .. .. .. .. .. .. ..\n" +
                "4|.. .. .. .. .. .. .. ..\n" +
                "3|.. .. .. .. .. .. .. ..\n" +
                "2|WP WP WP WP WP WP WP WP\n" +
                "1|WR WN WB WQ WK WB WN WR\n" +
                "  -- -- -- -- -- -- -- --\n" +
                "   a  b  c  d  e  f  g  h";
        assertEquals(representation, position.toString());
    }
}
