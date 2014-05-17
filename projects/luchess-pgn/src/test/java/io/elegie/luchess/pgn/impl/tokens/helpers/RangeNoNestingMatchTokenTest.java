package io.elegie.luchess.pgn.impl.tokens.helpers;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.Token;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to automate testing on tokens spanning a range, and not
 * allowing nesting (such as the value of a tag pair).
 */
@SuppressWarnings("javadoc")
public abstract class RangeNoNestingMatchTokenTest extends AbstractTokenTest {

    private static final Logger LOG = LoggerFactory.getLogger(RangeNoNestingMatchTokenTest.class);

    private final Class<? extends Token> tokenClass;
    private final TokenType tokenType;
    private final char startChar;
    private final char endChar;

    protected RangeNoNestingMatchTokenTest(Class<? extends Token> tokenClass, TokenType tokenType, int startChar,
            int endChar) {
        this.tokenClass = tokenClass;
        this.tokenType = tokenType;
        this.startChar = (char) startChar;
        this.endChar = (char) endChar;
    }

    protected char getStartChar() {
        return startChar;
    }

    protected char getEndChar() {
        return endChar;
    }

    @Override
    protected Token createToken() {
        try {
            return tokenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "Cannot create the token (%s).";
            message = String.format(message, e.getClass().getSimpleName());
            LOG.error(message, e);
            return null;
        }
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { startChar };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'Z' };
    }

    // --- Valid ranges -------------------------------------------------------

    @Test
    public void testSimpleRange() throws ParseException {
        testContent("foo");
    }

    @Test
    public void testRangeWithEscaping() throws ParseException {
        testContent("foo\\" + startChar);
        testContent("foo\\" + startChar + "bar");
        testContent("foo\\" + endChar);
        testContent("foo\\" + endChar + "bar");
    }

    protected void testContent(String content) throws ParseException {
        testMatch(tokenType, startChar + content + endChar, content);
    }

    // --- Unterminated ranges ------------------------------------------------

    @Test(expected = ParseException.class)
    public void testSimpleUnterminated() throws ParseException {
        testUnterminated("foo");
    }

    @Test(expected = ParseException.class)
    public void testUnterminatedWithEscaping() throws ParseException {
        testUnterminated("foo\\" + startChar);
    }

    protected void testUnterminated(String content) throws ParseException {
        testMatch(tokenType, startChar + content, content);
    }

}
