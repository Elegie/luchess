package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.AbstractTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class EndOfLineCommentTokenTest extends AbstractTokenTest {

    @Override
    protected Token createToken() {
        return new EndOfLineCommentToken();
    }

    @Override
    protected int[] getAcceptableValues() {
        return new int[] { ';' };
    }

    @Override
    protected int[] getNonAcceptableValues() {
        return new int[] { ',' };
    }

    @Test
    public void testSuccessEOS() throws ParseException {
        testMatch(TokenType.COMMENT, ";Hello, World!", "Hello, World!");
    }

    @Test
    public void testSuccessLF() throws ParseException {
        testMatch(TokenType.COMMENT, ";Hello, World!\n Ignored Part", "Hello, World!");
    }

    @Test
    public void testSuccessCR() throws ParseException {
        testMatch(TokenType.COMMENT, ";Hello, World!\r", "Hello, World!");
    }

    @Test
    public void testSuccessCRLF() throws ParseException {
        testMatch(TokenType.COMMENT, ";Hello, World!\r\n Ignored Part", "Hello, World!");
    }

}
