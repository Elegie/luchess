package io.elegie.luchess.pgn.impl.grammars;

import static org.junit.Assert.assertEquals;
import io.elegie.luchess.pgn.ParserFactory;
import io.elegie.luchess.pgn.api.OpenTokenVisitor;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;
import io.elegie.luchess.pgn.api.TokenType;

import java.io.IOException;
import java.io.StringReader;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test all possible moves against the Move Grammar - i.e. we want to validate
 * our grammar tree.
 * 
 * We use good old bit masks here, to check that appropriate visitor methods
 * (specified in the mask) have been properly called. Also, all moves are tested
 * using with and without king checks.
 */
@SuppressWarnings("javadoc")
public class MoveGrammarTest extends AbstractGrammarTest {

    private static final byte CASTLE = 1;
    private static final byte CAPTURE = 2;
    private static final byte CHECK = 4;
    private static final byte FIGURINE = 8;
    private static final byte PROMOTION = 16;
    private static final byte COLUMN = 32;
    private static final byte ROW = 64;

    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        parser = ParserFactory.createMoveParser();
    }

    @Override
    protected Parser getParser() {
        return parser;
    }

    // --- Castle Tests -------------------------------------------------------

    @Test
    public void testSmallCastle() throws IOException, ParseException {
        testMoveWithCheck("O-O", CASTLE);
    }

    @Test
    public void testBigCastle() throws IOException, ParseException {
        testMoveWithCheck("O-O-O", CASTLE);
    }

    @Test(expected = ParseException.class)
    public void testCastleErrorLowerCase() throws IOException, ParseException {
        testMoveWithCheck("o-o", CASTLE);
    }

    @Test(expected = ParseException.class)
    public void testCastleErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("O-O-", CASTLE);
    }

    @Test(expected = ParseException.class)
    public void testCastleErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("O-X", CASTLE);
    }

    // --- Pawn Tests ---------------------------------------------------------

    // Advance

    @Test
    public void testPawnAdvance() throws IOException, ParseException {
        testMoveWithCheck("e4", ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testPawnAdvanceErrorLowerCase() throws IOException, ParseException {
        testMoveWithCheck("E4", ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testPawnAdvanceErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("e", ROW | COLUMN);
    }

    // Capture

    @Test
    public void testPawnCapture() throws IOException, ParseException {
        testMoveWithCheck("exd4", ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testPawnCaptureErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("exd", ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testPawnCaptureErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("ex4", ROW | COLUMN | CAPTURE);
    }

    // Promotion

    @Test
    public void testPawnPromotion() throws IOException, ParseException {
        testMoveWithCheck("e8=Q", ROW | COLUMN | PROMOTION | FIGURINE);
    }

    @Test(expected = ParseException.class)
    public void testPawnPromotionErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("e8=", ROW | COLUMN | PROMOTION | FIGURINE);
    }

    @Test(expected = ParseException.class)
    public void testPawnPromotionErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("e8=Z", ROW | COLUMN | PROMOTION | FIGURINE);
    }

    // Promotion and Capture

    @Test
    public void testPawnPromotionAndCapture() throws IOException, ParseException {
        testMoveWithCheck("exf8=N", ROW | COLUMN | CAPTURE | PROMOTION | FIGURINE);
    }

    @Test(expected = ParseException.class)
    public void testPawnPromotionAndCaptureErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("exf8=", ROW | COLUMN | CAPTURE | PROMOTION | FIGURINE);
    }

    @Test(expected = ParseException.class)
    public void testPawnPromotionAndCaptureErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("exf8=Z", ROW | COLUMN | CAPTURE | PROMOTION | FIGURINE);
    }

    // --- Figurine Tests -----------------------------------------------------

    // Move

    @Test
    public void testFigurineMove() throws IOException, ParseException {
        testMoveWithCheck("Kf3", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveErrorLowerCase() throws IOException, ParseException {
        testMoveWithCheck("nf3", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("Nf", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("Nfz", FIGURINE | ROW | COLUMN);
    }

    // Move with row disambiguated

    @Test
    public void testFigurineMoveRowDisambiguated() throws IOException, ParseException {
        testMoveWithCheck("Q2a3", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveRowDisambiguatedErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("N2e", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveRowDisambiguatedErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("N2ez", FIGURINE | ROW | COLUMN);
    }

    // Move with col disambiguated

    @Test
    public void testFigurineMoveColDisambiguated() throws IOException, ParseException {
        testMoveWithCheck("Nbc4", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveColDisambiguatedErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("Nfe", FIGURINE | ROW | COLUMN);
    }

    @Test(expected = ParseException.class)
    public void testFigurineMoveColDisambiguatedErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("Nfez", FIGURINE | ROW | COLUMN);
    }

    // Capture

    @Test
    public void testFigurineCapture() throws IOException, ParseException {
        testMoveWithCheck("Bxf3", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("Bxf", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("Bxf9", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    // Capture with row disambiguated

    @Test
    public void testFigurineCaptureRowDisambiguated() throws IOException, ParseException {
        testMoveWithCheck("R2xf3", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureRowDisambiguatedErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("R2xf", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureRowDisambiguatedErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("R2xf9", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    // Capture with col disambiguated

    @Test
    public void testFigurineCaptureColDisambiguated() throws IOException, ParseException {
        testMoveWithCheck("Rexf3", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureColDisambiguatedErrorIncomplete() throws IOException, ParseException {
        testMoveWithCheck("Rexf", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    @Test(expected = ParseException.class)
    public void testFigurineCaptureColDisambiguatedErrorUnrecognized() throws IOException, ParseException {
        testMoveWithCheck("Rexf9", FIGURINE | ROW | COLUMN | CAPTURE);
    }

    // --- Helpers ------------------------------------------------------------

    private void testMoveWithCheck(String move, int modifiers) throws IOException, ParseException {
        testMove(new OpenTokenVisitor(), move, modifiers);
        testMove(new OpenTokenVisitor(), move + "+", modifiers | CHECK);
        testMove(new OpenTokenVisitor(), move + "#", modifiers | CHECK);
    }

    private void testMove(OpenTokenVisitor visitor, String move, int modifiers) throws IOException, ParseException {
        getParser().digest(new StringReader(move), visitor);
        assertEquals("castleVisited for " + move, contains(modifiers, CASTLE),
                hasValue(visitor.getValue(TokenType.CASTLE)));
        assertEquals("captureVisited for " + move, contains(modifiers, CAPTURE),
                hasValue(visitor.getValue(TokenType.CAPTURE)));
        assertEquals("checkVisited for " + move, contains(modifiers, CHECK),
                hasValue(visitor.getValue(TokenType.CHECK)));
        assertEquals("figurineVisited for " + move, contains(modifiers, FIGURINE),
                hasValue(visitor.getValue(TokenType.FIGURINE)));
        assertEquals("promotionVisited for " + move, contains(modifiers, PROMOTION),
                hasValue(visitor.getValue(TokenType.PROMOTION)));
        assertEquals("columnVisited for " + move, contains(modifiers, COLUMN),
                hasValue(visitor.getValue(TokenType.COLUMN)));
        assertEquals("rowVisited for " + move, contains(modifiers, ROW), hasValue(visitor.getValue(TokenType.ROW)));
    }

    private boolean contains(int modifiers, int reference) {
        return (modifiers & reference) == reference;
    }

    private boolean hasValue(String variable) {
        return variable != null && !variable.isEmpty();
    }

}
