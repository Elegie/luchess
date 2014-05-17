package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CheckTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new CheckToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { '#', '+' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '-' };
    }

    @Test
    public void testCheckSuccess() throws ParseException {
        testMatch(TokenType.CHECK, "+");
        testMatch(TokenType.CHECK, "#");
    }

}
