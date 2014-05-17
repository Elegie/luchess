package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class NAGTokenTest extends OneMatchTokenTest {

    public NAGTokenTest() {
        super(NAGToken.class, TokenType.NAG, '$', "$42");
    }

    @Test(expected = ParseException.class)
    public void testMissingQualifier() throws ParseException {
        testMatch(TokenType.NAG, "$");
    }

    @Test(expected = ParseException.class)
    public void testNaNQualifier() throws ParseException {
        testMatch(TokenType.NAG, "$FOO");
    }

}
