package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class Dot3TokenTest extends OneMatchTokenTest {

    public Dot3TokenTest() {
        super(Dot3Token.class, null, '.', "...");
    }

    @Test(expected = ParseException.class)
    public void testOneDot() throws ParseException {
        testMatch(null, ".");
    }

    @Test(expected = ParseException.class)
    public void testTwoDot() throws ParseException {
        testMatch(null, "..");
    }

}
