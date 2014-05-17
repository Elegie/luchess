package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SpaceSeparatorTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new SpaceSeparatorToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { ' ', '\n', '\r', '\t' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'a' };
    }

    @Test
    public void testWhiteSpaceSuccess() throws ParseException {
        testAcceptableValues(null);
        testMatch(null, " \n\r\t");
    }

    @Test
    public void testTruncation() throws ParseException {
        testMatch(null, " \n\r\tfoo", " \n\r\t");
    }
}
