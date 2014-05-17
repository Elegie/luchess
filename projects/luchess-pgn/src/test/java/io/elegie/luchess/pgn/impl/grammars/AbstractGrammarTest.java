package io.elegie.luchess.pgn.impl.grammars;

import io.elegie.luchess.pgn.api.OpenTokenVisitor;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.junit.Test;

/**
 * Includes tests that should be passed by all grammars.
 */
public abstract class AbstractGrammarTest {

    private static final String BINARY_FILE = "/binary.7z";

    protected abstract Parser getParser();

    /**
     * Make sure that a grammar does not choke over a binary file.
     * 
     * @throws IOException
     *             When the file is not readable.
     * @throws ParseException
     *             When the file is not a PGN file (this is what we expect).
     */
    @Test(expected = ParseException.class)
    public void testBinary() throws IOException, ParseException {
        final URL url = SimpleGameGrammarTest.class.getResource(BINARY_FILE);
        final File file = new File(url.getPath());
        try (Reader reader = new FileReader(file)) {
            getParser().digest(reader, new OpenTokenVisitor());
        }
    }

}
