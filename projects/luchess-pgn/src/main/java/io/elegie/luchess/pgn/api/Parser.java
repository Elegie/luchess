package io.elegie.luchess.pgn.api;

import java.io.IOException;
import java.io.Reader;

/**
 * <p>
 * The parser is the main operating unit. It is based on a combination of
 * Interpreter and Visitor design patterns. Basically, the client creates a
 * Parser (using the {@link io.elegie.luchess.pgn.ParserFactory}), then invokes
 * the {@link #digest(Reader, ParserVisitor)} method.
 * </p>
 * 
 * <p>
 * A parser should be configured with a
 * {@link io.elegie.luchess.pgn.impl.grammars.Grammar}. The client should
 * however never manipulate grammars directly, and rather obtain the desired
 * parser implementation using the {@link io.elegie.luchess.pgn.ParserFactory}.
 * </p>
 */
public interface Parser {

    /**
     * The main method of the Parser. Implementations should make sure that this
     * method remains thread-safe.
     * 
     * @param reader
     *            A reader to the PGN source stream. This reader will be
     *            consumed by the Parser, so the client should make sure that it
     *            is properly positioned. The Parser only moves forward while
     *            consuming the stream, never backward.
     * @param visitor
     *            Each time the parser recognizes a certain PGN sequence, like a
     *            move, it calls the corresponding method on the visitor. This
     *            way, the client can use the visitor to construct its own
     *            object representation of what is being parsed.
     * @throws IOException
     * @throws ParseException
     */
    void digest(Reader reader, ParserVisitor visitor) throws IOException, ParseException;

}
