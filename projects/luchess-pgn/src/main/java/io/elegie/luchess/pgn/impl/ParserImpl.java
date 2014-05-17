package io.elegie.luchess.pgn.impl;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;
import io.elegie.luchess.pgn.api.ParserVisitor;
import io.elegie.luchess.pgn.impl.grammars.Grammar;
import io.elegie.luchess.pgn.impl.tokens.Token;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * <p>
 * This is the main implementation of the Parser. It is constructed with a
 * {@link io.elegie.luchess.pgn.impl.grammars.Grammar}, which provides the way
 * the PGN nodes (tokens) can be combined together. The implementation reads
 * each character off the input stream, and checks it against a current node of
 * the tree - if the node accepts the char, then we move on to the next nodes
 * (if any), otherwise we will throw a parsing exception. The parsing is done
 * when the tree has been fully consumed.
 * </p>
 * 
 * <p>
 * For instance, let us suppose the following tree:
 * </p>
 * 
 * <pre>
 * RootToken (starting empty point)
 * |
 * |__Number1Token
 *    |
 *    |__ColumnToken
 * </pre>
 * 
 * <p>
 * If the input stream starts with "1a", then "1a" will be parsed successfully,
 * as it is a {@link io.elegie.luchess.pgn.impl.tokens.Number1Token} followed
 * with a {@link io.elegie.luchess.pgn.impl.tokens.ColumnToken} - other
 * combinations would fail. Each time a valid token is recognized by the parser,
 * then the appropriate {@link io.elegie.luchess.pgn.api.ParserVisitor} method
 * is called.
 * </p>
 */
public class ParserImpl implements Parser {

    /**
     * The first node of the grammar should always be a RootToken. Such a token
     * has no value, it simply acts as the parent token for the whole tree. Our
     * process is recursive though, so we need to pass the last character read
     * by the stream, so as to avoid back-pedaling. The DEFAULT_START_CHAR is a
     * dummy (to be ignored) value, intended for the RootToken.
     */
    private static final int DEFAULT_START_CHAR = 0;

    private final Token root;

    /**
     * The parser should be thread-safe, provided that the passed grammar is
     * thread-safe itself.
     * 
     * @param grammar
     *            The grammar against which the input will be analyzed.
     */
    public ParserImpl(Grammar grammar) {
        this.root = grammar.getRoot();
    }

    @Override
    public void digest(Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        digest(root, DEFAULT_START_CHAR, reader, visitor);
    }

    // --- Helpers ----------------------------------------------------------

    /**
     * The recursive implementation of the parsing. If the current read data is
     * matched by the current token, then we continue the parsing on the first
     * successor token accepting the next read data. If no successor has been
     * found, then this means that the parsing is finished. On the opposite, if
     * some successors are available, but do not accept the char, then this
     * means that the char is not authorized at this place by the grammar - this
     * is a parsing exception.
     * 
     * @param node
     *            The current node of the grammar, which should match the passed
     *            data.
     * @param data
     *            The current character being analyzed from the input stream.
     * @param reader
     *            The input stream from which to read the data.
     * @param visitor
     *            The visitor to be notified when a token accepts a data.
     * @throws IOException
     *             When the reader cannot be read.
     * @throws ParseException
     *             When the current character data is not accepted by the
     *             current node of the grammar.
     */
    private void digest(Token node, int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        int currentData = node.interpret(data, reader, visitor);
        List<Token> successors = node.getSuccessors();
        if (successors.isEmpty()) {
            return;
        }
        for (Token successor : successors) {
            if (successor.accepts(currentData)) {
                digest(successor, currentData, reader, visitor);
                return;
            }
        }
        terminateWithFailure(node, currentData);
    }

    private void terminateWithFailure(Token lastNode, int lastData) throws ParseException {
        String type = lastNode.getClass().getSimpleName();
        String message = "Cannot parse successors for %s (code=%s, value=\"%s\")";
        message = String.format(message, type, lastData, (char) lastData);
        throw new ParseException(message);
    }
}
