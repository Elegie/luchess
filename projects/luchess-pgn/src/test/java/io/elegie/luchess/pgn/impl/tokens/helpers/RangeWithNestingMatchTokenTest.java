package io.elegie.luchess.pgn.impl.tokens.helpers;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.Token;

import org.junit.Test;

/**
 * Builds on RangeNoNestingMatchTokenTest to allow for nesting (such as comments
 * of variations).
 */
@SuppressWarnings("javadoc")
public abstract class RangeWithNestingMatchTokenTest extends RangeNoNestingMatchTokenTest {

    protected RangeWithNestingMatchTokenTest(Class<? extends Token> tokenClass, TokenType tokenType, int startChar,
            int endChar) {
        super(tokenClass, tokenType, startChar, endChar);
    }

    // --- Valid ranges -------------------------------------------------------

    @Test
    public void testRangeWithNesting() throws ParseException {
        testContent("foo" + getStartChar() + "bar" + getEndChar());
        testContent("foo" + getStartChar() + "bar" + getStartChar() + "bar" + getEndChar() + getEndChar());
    }

    @Test
    public void testRangeWithEscapingAndNesting() throws ParseException {
        testContent("foo" + getStartChar() + "bar\\" + getStartChar() + getEndChar());
    }

    // --- Unterminated ranges ------------------------------------------------

    @Test(expected = ParseException.class)
    public void testUnterminatedWithNesting() throws ParseException {
        testUnterminated("foo" + getStartChar() + "bar" + getEndChar());
    }

    @Test(expected = ParseException.class)
    public void testUnterminatedWithEscapingAndNesting() throws ParseException {
        testUnterminated("foo" + getStartChar() + "bar\\" + getStartChar() + getEndChar());
    }

}
