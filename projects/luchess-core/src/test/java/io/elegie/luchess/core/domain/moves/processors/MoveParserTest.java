package io.elegie.luchess.core.domain.moves.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.helpers.MoveHelper;
import io.elegie.luchess.core.domain.moves.AbstractFigurineMove;
import io.elegie.luchess.core.domain.moves.BishopMove;
import io.elegie.luchess.core.domain.moves.CastleMove;
import io.elegie.luchess.core.domain.moves.KingMove;
import io.elegie.luchess.core.domain.moves.KnightMove;
import io.elegie.luchess.core.domain.moves.PawnCaptureMove;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;
import io.elegie.luchess.core.domain.moves.QueenMove;
import io.elegie.luchess.core.domain.moves.RookMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests that all moves are created correctly. As for figurine moves, only the
 * QueenMove is fully explored, as its variations also apply to other figurine
 * moves.
 */
@SuppressWarnings("javadoc")
public class MoveParserTest {

    // --- Test Castle Move ---------------------------------------------------

    @Test
    public void testCastleSmall() {
        CastleMove move = new CastleMove(true);
        Move parsed = MoveParser.convert("O-O");
        assertEquals(move, parsed);
    }

    @Test
    public void testCastleLarge() {
        CastleMove move = new CastleMove(false);
        Move parsed = MoveParser.convert("O-O-O");
        assertEquals(move, parsed);
    }

    // --- Test Pawn Forward Move ---------------------------------------------

    @Test
    public void testPawnForwardSimple() {
        PawnForwardMove move = new PawnForwardMove();
        move.setTarget(new Vertex('e', 4));
        Move parsed = MoveParser.convert("e4");
        assertEquals(move, parsed);
    }

    @Test
    public void testPawnForwardPromotion() {
        PawnForwardMove move = new PawnForwardMove();
        move.setTarget(new Vertex('e', 8));
        move.setPromotion(Figurine.QUEEN);
        Move parsed = MoveParser.convert("e8=Q");
        assertEquals(move, parsed);
    }

    // --- Test Pawn Capture Move ---------------------------------------------

    @Test
    public void testPawnCaptureSimple() {
        PawnCaptureMove move = new PawnCaptureMove();
        move.setDisambiguation(new Vertex('e', 0));
        move.setTarget(new Vertex('f', 4));
        Move parsed = MoveParser.convert("exf4");
        assertEquals(move, parsed);
    }

    @Test
    public void testPawnCapturePromotion() {
        PawnCaptureMove move = new PawnCaptureMove();
        move.setDisambiguation(new Vertex('e', 0));
        move.setTarget(new Vertex('f', 8));
        move.setPromotion(Figurine.QUEEN);
        Move parsed = MoveParser.convert("exf8=Q");
        assertEquals(move, parsed);
    }

    // --- Test King Move -----------------------------------------------------

    @Test
    public void testKingSimple() {
        testFigurineSimple(new KingMove());
    }

    @Test
    public void testKingCapture() {
        KingMove move = new KingMove();
        move.setTarget(new Vertex('e', 4));
        move.setCaptures(true);
        Move parsed = MoveParser.convert("Kxe4");
        assertEquals(move, parsed);
    }

    // --- Test Queen Move (all cases) ----------------------------------------

    @Test
    public void testQueenSimple() {
        testFigurineSimple(new QueenMove());
    }

    @Test
    public void testQueenSimpleCheck() {
        QueenMove move = new QueenMove();
        move.setTarget(new Vertex('e', 4));
        move.setCheck('+');
        Move parsed = MoveParser.convert("Qe4+");
        assertEquals(move, parsed);
    }

    @Test
    public void testQueenSimpleDisambiguatedRow() {
        QueenMove move = new QueenMove();
        move.setDisambiguation(new Vertex('z', 3));
        move.setTarget(new Vertex('e', 4));
        Move parsed = MoveParser.convert("Q3e4");
        assertEquals(move, parsed);
    }

    @Test
    public void testQueenSimpleDisambiguatedCol() {
        QueenMove move = new QueenMove();
        move.setDisambiguation(new Vertex('d', 0));
        move.setTarget(new Vertex('e', 4));
        Move parsed = MoveParser.convert("Qde4");
        assertEquals(move, parsed);
    }

    @Test
    public void testQueenSimpleDisambiguatedRowCol() {
        QueenMove move = new QueenMove();
        move.setDisambiguation(new Vertex('d', 5));
        move.setTarget(new Vertex('e', 4));
        Move parsed = MoveParser.convert("Qd5e4");
        assertEquals(move, parsed);
    }

    @Test
    public void testQueenCapture() {
        QueenMove move = new QueenMove();
        move.setTarget(new Vertex('e', 4));
        move.setCaptures(true);
        Move parsed = MoveParser.convert("Qxe4");
        assertEquals(move, parsed);
    }

    @Test
    public void testQueenCaptureDisambiguatedRowCol() {
        QueenMove move = new QueenMove();
        move.setDisambiguation(new Vertex('d', 5));
        move.setTarget(new Vertex('e', 4));
        move.setCaptures(true);
        Move parsed = MoveParser.convert("Qd5xe4");
        assertEquals(move, parsed);
    }

    // --- Test other figurines -----------------------------------------------

    @Test
    public void testKnightSimple() {
        testFigurineSimple(new KnightMove());
    }

    @Test
    public void testBishopSimple() {
        testFigurineSimple(new BishopMove());
    }

    @Test
    public void testRookSimple() {
        testFigurineSimple(new RookMove());
    }

    // --- Test List ----------------------------------------------------------

    @Test
    public void testEmptyList() {
        List<String> source = new ArrayList<>();
        List<Move> parsed = MoveParser.convert(source);
        assertTrue(parsed.isEmpty());
    }

    @Test
    public void testMovesList() {
        List<Move> moves = new ArrayList<>();
        moves.add(MoveHelper.createPawnForwardMove('e', 4));
        moves.add(MoveHelper.createPawnForwardMove('d', 5));

        List<String> source1 = Arrays.asList("e4", "d5", null, "c6");
        List<Move> parsed = MoveParser.convert(source1);
        assertEquals(moves, parsed);

        List<String> source2 = Arrays.asList("e4", "d5", "", "c6");
        parsed = MoveParser.convert(source2);
        assertEquals(moves, parsed);
    }

    // --- Helpers ------------------------------------------------------------

    private void testFigurineSimple(AbstractFigurineMove move) {
        move.setTarget(new Vertex('e', 4));
        Move parsed = MoveParser.convert(move.getFigurine().toString() + "e4");
        assertEquals(move, parsed);
    }
}
