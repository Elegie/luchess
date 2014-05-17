package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class NumberTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new NumberToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'a' };
    }

    @Test
    public void testNumberSuccess() throws ParseException {
        testAcceptableValues(null);
        testMatch(null, "1234567890");
    }

    @Test
    public void testTruncation() throws ParseException {
        testMatch(null, "1x2", "1");
    }

}
