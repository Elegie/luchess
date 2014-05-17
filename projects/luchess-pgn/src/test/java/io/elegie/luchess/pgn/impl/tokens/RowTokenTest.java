package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class RowTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new RowToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { '1', '2', '3', '4', '5', '6', '7', '8' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '0', '9' };
    }

    @Test
    public void testRowSuccess() throws ParseException {
        testAcceptableValues(TokenType.ROW);
    }

}
