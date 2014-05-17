package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class UnstructuredMoveTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new UnstructuredMoveToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { 'O', 'K', 'Q', 'B', 'N', 'R', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { '!', 'A', 'o', '?' };
    }

    @Test
    public void testMoveSuccess() throws ParseException {
        testAcceptableValues(TokenType.UNSTRUCTURED_MOVE);
        testMatch(TokenType.UNSTRUCTURED_MOVE, "O-KQNBRabcdefgh12345678x=+#");
    }

}
