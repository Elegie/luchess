package io.elegie.luchess.pgn;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.pgn.api.Parser;

import org.junit.Test;

/**
 * Checks that parsers are correctly created through the factory. Note that we
 * are not testing grammars here.
 */
@SuppressWarnings("javadoc")
public class ParserFactoryTest {

    @Test
    public void testCreateSimpleGameParser() {
        assertParserNotNull(ParserFactory.createSimpleGameParser());
    }

    @Test
    public void testCreateMoveParser() {
        assertParserNotNull(ParserFactory.createMoveParser());
    }

    private static void assertParserNotNull(Parser parser) {
        assertNotNull("Parser is not null", parser);
    }
}
