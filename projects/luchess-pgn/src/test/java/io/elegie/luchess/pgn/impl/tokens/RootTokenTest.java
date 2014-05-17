package io.elegie.luchess.pgn.impl.tokens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.pgn.api.ParseException;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class RootTokenTest {

    private Token root;

    @Before
    public void setUp() {
        root = new RootToken();
    }

    @Test
    public void testAcceptAll() {
        assertTrue(root.accepts('a'));
        assertTrue(root.accepts('1'));
        assertTrue(root.accepts(-1));
    }

    @Test
    public void testAdvance() throws IOException, ParseException {
        assertEquals('f', root.interpret(0, new StringReader("foo"), null));
    }

    @Test
    public void testNoAdvance() throws IOException, ParseException {
        assertEquals('f', root.interpret('f', new StringReader("bar"), null));
    }

}
