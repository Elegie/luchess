package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class EndOfFileTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new EndOfFileToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { -1 };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'a' };
    }

    @Test
    public void testEOFSuccess() throws ParseException {
        testMatch(null, "", null);
    }

}
