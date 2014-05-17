package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TerminationWithNumber1TokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new TerminationWithNumber1Token();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { '-', '/' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '1', '0', '*' };
    }

    @Test
    public void testWhiteWinsSuccess() throws ParseException {
        testMatch(TokenType.TERMINATION, "-0", "1-0");
    }

    @Test
    public void testDrawSuccess() throws ParseException {
        testMatch(TokenType.TERMINATION, "/2-1/2", "1/2-1/2");
    }

    // --- Invalid tokens -----------------------------------------------------

    @Test(expected = ParseException.class)
    public void testWhiteWinsInvalid() throws ParseException {
        testMatch(TokenType.TERMINATION, "-X");
    }

    @Test(expected = ParseException.class)
    public void testDrawInvalid() throws ParseException {
        testMatch(TokenType.TERMINATION, "/X");
    }

    @Test(expected = ParseException.class)
    public void testUnknown() throws ParseException {
        testMatch(TokenType.TERMINATION, "X");
    }

    // --- Unterminated tokens ------------------------------------------------

    @Test(expected = ParseException.class)
    public void testWhiteWinsUnterminated() throws ParseException {
        testMatch(TokenType.TERMINATION, "-");
    }

    @Test(expected = ParseException.class)
    public void testDrawUnterminated() throws ParseException {
        testMatch(TokenType.TERMINATION, "/2-1");
    }

}
