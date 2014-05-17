package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TerminationWithoutNumber1TokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new TerminationWithoutNumber1Token();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { '0', '*' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '/', '-' };
    }

    @Test
    public void testBlackWinsSuccess() throws ParseException {
        testMatch(TokenType.TERMINATION, "0-1");
    }

    @Test
    public void testUnfinishedSuccess() throws ParseException {
        testMatch(TokenType.TERMINATION, "*");
    }

    // --- Invalid tokens -----------------------------------------------------

    @Test(expected = ParseException.class)
    public void testBlackWinsInvalid() throws ParseException {
        testMatch(TokenType.TERMINATION, "0X");
    }

    // --- Unterminated tokens ------------------------------------------------

    @Test(expected = ParseException.class)
    public void testBlackWinsUnterminated() throws ParseException {
        testMatch(TokenType.TERMINATION, "0-");
    }

}
