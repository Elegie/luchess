package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * A board is merely a wrapper for a position, so there is not a lot that we can
 * test here, except basic integration tests.
 */
public class BoardTest {

    /**
     * We play a few moves, and check that the position contained within the
     * board has been properly updated.
     * 
     * @throws IllegalMoveException
     *             When an invalid move has been requested.
     */
    @Test
    public void testMovesPlayed() throws IllegalMoveException {
        List<Move> moves = new ArrayList<>();
        final char col = 'e';
        final int startWhiteRow = 2;
        final int endWhiteRow = 4;
        final int startBlackRow = 7;
        final int endBlackRow = 5;

        PawnForwardMove p1 = new PawnForwardMove();
        p1.setDisambiguation(new Vertex(col, startWhiteRow));
        p1.setTarget(new Vertex(col, endWhiteRow));
        moves.add(p1);

        PawnForwardMove p2 = new PawnForwardMove();
        p2.setDisambiguation(new Vertex(col, startBlackRow));
        p2.setTarget(new Vertex(col, endBlackRow));
        moves.add(p2);

        Board board = new Board();
        board.play(moves);
        assertEquals(new Piece(Figurine.PAWN, Color.WHITE), board.getPiece(col, endWhiteRow));
        assertEquals(new Piece(Figurine.PAWN, Color.BLACK), board.getPiece(col, endBlackRow));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testEqualsHashcode() {
        Board board1 = new Board();
        Board board2 = new Board();
        assertEquals(board1, board1);
        assertFalse(board1.equals(null));
        assertEquals(board1, board2);
        assertEquals(new Board().hashCode(), new Board().hashCode());
    }

}
