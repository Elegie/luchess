package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class FigurineTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new FigurineToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { 'R', 'N', 'B', 'Q', 'K' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'r', 'n', 'b', 'q', 'k' };
    }

    @Test
    public void testFigurineSuccess() throws ParseException {
        testAcceptableValues(TokenType.FIGURINE);
    }

}
