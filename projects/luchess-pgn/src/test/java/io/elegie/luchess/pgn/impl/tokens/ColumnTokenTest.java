package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ColumnTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new ColumnToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
    }

    @Test
    public void testColumnSuccess() throws ParseException {
        testAcceptableValues(TokenType.COLUMN);
    }

}
