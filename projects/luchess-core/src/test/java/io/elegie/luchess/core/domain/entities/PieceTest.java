package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class PieceTest {

    @Test
    public void testStructure() {
        Piece whitePawn = new Piece(Figurine.PAWN, Color.WHITE);
        assertEquals(whitePawn.getFigurine(), Figurine.PAWN);
        assertEquals(whitePawn.getColor(), Color.WHITE);
    }

    @Test
    public void testPieceParsing() {
        assertEquals(new Piece(Figurine.PAWN, Color.WHITE), Piece.parse("WP"));
        assertEquals(new Piece(Figurine.ROOK, Color.WHITE), Piece.parse("WR"));
        assertEquals(new Piece(Figurine.KNIGHT, Color.WHITE), Piece.parse("WN"));
        assertEquals(new Piece(Figurine.BISHOP, Color.WHITE), Piece.parse("WB"));
        assertEquals(new Piece(Figurine.QUEEN, Color.WHITE), Piece.parse("WQ"));
        assertEquals(new Piece(Figurine.KING, Color.WHITE), Piece.parse("WK"));
        assertEquals(new Piece(Figurine.PAWN, Color.BLACK), Piece.parse("BP"));
        assertEquals(new Piece(Figurine.ROOK, Color.BLACK), Piece.parse("BR"));
        assertEquals(new Piece(Figurine.KNIGHT, Color.BLACK), Piece.parse("BN"));
        assertEquals(new Piece(Figurine.BISHOP, Color.BLACK), Piece.parse("BB"));
        assertEquals(new Piece(Figurine.QUEEN, Color.BLACK), Piece.parse("BQ"));
        assertEquals(new Piece(Figurine.KING, Color.BLACK), Piece.parse("BK"));
        assertNull(Piece.parse("foo"));
    }

    @Test
    public void testPieceCompare() {
        Piece whitePawn = new Piece(Figurine.PAWN, Color.WHITE);
        assertFalse(Piece.compare(null, null, null));
        assertFalse(Piece.compare(whitePawn, Figurine.BISHOP, null));
        assertFalse(Piece.compare(whitePawn, Figurine.PAWN, Color.BLACK));
        assertTrue(Piece.compare(whitePawn, Figurine.PAWN, Color.WHITE));
    }

    @Test
    public void testEquals() {
        Piece whitePawn1 = new Piece(Figurine.PAWN, Color.WHITE);
        Piece whitePawn2 = new Piece(Figurine.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(Figurine.PAWN, Color.BLACK);
        Piece whiteQueen = new Piece(Figurine.QUEEN, Color.WHITE);

        assertEquals(whitePawn1, whitePawn1);
        assertEquals(whitePawn1, whitePawn2);
        assertTrue(whitePawn1.hashCode() == whitePawn2.hashCode());
        assertFalse(whitePawn1.equals(Figurine.PAWN));
        assertFalse(whitePawn1.equals(blackPawn));
        assertFalse(whitePawn1.equals(whiteQueen));
    }

}
