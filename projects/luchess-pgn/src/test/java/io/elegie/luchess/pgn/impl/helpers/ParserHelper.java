package io.elegie.luchess.pgn.impl.helpers;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;
import io.elegie.luchess.pgn.api.ParserVisitor;
import io.elegie.luchess.pgn.impl.grammars.SimpleGameGrammarTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Helpers to load and parse games from an external resource.
 */
public final class ParserHelper {

    private ParserHelper() {

    }

    /**
     * @param resource
     *            Path to the resource.
     * @param parser
     *            Parser to be used.
     * @param visitor
     *            Visitor to be used.
     * @throws FileNotFoundException
     *             When the resource cannot be found.
     * @throws IOException
     *             When the resource cannot be read.
     * @throws ParseException
     *             When the resource cannot be parsed.
     */
    public static void testResource(String resource, Parser parser, ParserVisitor visitor)
            throws FileNotFoundException, IOException, ParseException {
        parser.digest(load(resource), visitor);
    }

    /**
     * @param resource
     *            Path to the resource.
     * @return A reader for the resource.
     * @throws FileNotFoundException
     *             When the resource cannot be found.
     */
    private static Reader load(String resource) throws FileNotFoundException {
        final URL url = SimpleGameGrammarTest.class.getResource("/" + resource);
        final Scanner scanner = new Scanner(new File(url.getPath()), "UTF8");
        scanner.useDelimiter("\\Z");
        final String content = scanner.next();
        scanner.close();
        return new StringReader(content);
    }

}
