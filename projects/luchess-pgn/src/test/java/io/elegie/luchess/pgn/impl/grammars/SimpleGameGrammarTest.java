package io.elegie.luchess.pgn.impl.grammars;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.pgn.ParserFactory;
import io.elegie.luchess.pgn.api.OpenTokenVisitor;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.helpers.ParserHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * We want to test the game grammar here, and provide many examples in resource
 * files.
 */
@SuppressWarnings("javadoc")
public class SimpleGameGrammarTest extends AbstractGrammarTest {

    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        parser = ParserFactory.createSimpleGameParser();
    }

    @Override
    protected Parser getParser() {
        return parser;
    }

    // --- Valid games --------------------------------------------------------

    @Test
    public void testValidGame() throws IOException, ParseException {
        final OpenTokenVisitor visitor = testResource("game.valid.complete");
        assertNotNull("tagPairNameVisited", visitor.getValue(TokenType.TAG_PAIR_NAME));
        assertNotNull("tagPairValueVisited", visitor.getValue(TokenType.TAG_PAIR_VALUE));
        assertNotNull("NAGVisited", visitor.getValue(TokenType.NAG));
        assertNotNull("commentVisited", visitor.getValue(TokenType.COMMENT));
        assertNotNull("variationVisited", visitor.getValue(TokenType.VARIATION));
        assertNotNull("unstructuredMoveVisited", visitor.getValue(TokenType.UNSTRUCTURED_MOVE));
        assertNotNull("terminationVisited", visitor.getValue(TokenType.TERMINATION));
    }

    @Test
    public void testStandardGame() throws IOException, ParseException {
        testStandardGame("game.valid.standard.1");
        testStandardGame("game.valid.standard.2");
    }

    private void testStandardGame(String resource) throws IOException, ParseException {
        final OpenTokenVisitor visitor = testResource(resource);
        assertNotNull("tagPairNameVisited", visitor.getValue(TokenType.TAG_PAIR_NAME));
        assertNotNull("tagPairValueVisited", visitor.getValue(TokenType.TAG_PAIR_VALUE));
        assertNotNull("unstructuredMoveVisited", visitor.getValue(TokenType.UNSTRUCTURED_MOVE));
        assertNotNull("terminationVisited", visitor.getValue(TokenType.TERMINATION));
    }

    // --- Invalid Games (unrecognized) ---------------------------------------

    @Test(expected = ParseException.class)
    public void testInvalidGameUnrecognized() throws IOException, ParseException {
        testResource("game.invalid.unrecognized");
    }

    // --- Invalid Games (tag pair) -------------------------------------------

    @Test(expected = ParseException.class)
    public void testInvalidGameTagPairMissingName() throws IOException, ParseException {
        testResource("game.invalid.tagpair.missing.name");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameTagPairMissingValue() throws IOException, ParseException {
        testResource("game.invalid.tagpair.missing.value");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameTagPairUnfinished() throws IOException, ParseException {
        testResource("game.invalid.tagpair.unfinished");
    }

    // --- Invalid Games (move text) ------------------------------------------

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextWrongStart() throws IOException, ParseException {
        testResource("game.invalid.movetext.wrongstart");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextMissingMoveNumber() throws IOException, ParseException {
        testResource("game.invalid.movetext.missingmovenumber");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextMissingDotSeparator() throws IOException, ParseException {
        testResource("game.invalid.movetext.missingdotseparator");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextMissingSecondMove() throws IOException, ParseException {
        testResource("game.invalid.movetext.missingsecondmove");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextMissingTermination() throws IOException, ParseException {
        testResource("game.invalid.movetext.missingtermination");
    }

    @Test(expected = ParseException.class)
    public void testInvalidGameMoveTextTrashHereAndThere() throws IOException, ParseException {
        testResource("game.invalid.movetext.trash");
    }

    // --- Helpers ------------------------------------------------------------

    private static OpenTokenVisitor testResource(String resource) throws FileNotFoundException, IOException,
            ParseException {
        final OpenTokenVisitor visitor = new OpenTokenVisitor();
        ParserHelper.testResource(resource, parser, visitor);
        return visitor;
    }

}
