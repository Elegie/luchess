package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

/**
 * Tests all flavors of castles, from the usual valid castle (O-O, O-O-O) to
 * many invalid variations. Note that the letter 'O' in the castle notation
 * should always be in upper case.
 */
@SuppressWarnings("javadoc")
public class CastleTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new CastleToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { 'O' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'o' };
    }

    @Test
    public void testCastleSuccess() throws ParseException {
        testCastleMatch("O-O");
        testCastleMatch("O-O-O");
    }

    @Test(expected = ParseException.class)
    public void testFirstHyphenMissing() throws ParseException {
        testCastleMatch("O");
    }

    @Test(expected = ParseException.class)
    public void testFirstHyphenWrong() throws ParseException {
        testCastleMatch("OZ");
    }

    @Test(expected = ParseException.class)
    public void testSecondOMissing() throws ParseException {
        testCastleMatch("O-");
    }

    @Test(expected = ParseException.class)
    public void testThirdOMissing() throws ParseException {
        testCastleMatch("O-O-");
    }

    @Test(expected = ParseException.class)
    public void testThirdOWrong() throws ParseException {
        testCastleMatch("O-O-Z");
    }

    private void testCastleMatch(String value) throws ParseException {
        testMatch(TokenType.CASTLE, value);
    }

}
