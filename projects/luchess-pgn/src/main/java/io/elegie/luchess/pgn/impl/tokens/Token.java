package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * <p>
 * A token represents a PGN entity, such as a move, a player or a chess event.
 * Tokens can be combined together according to the rules of a PGN grammar. They
 * are always case-sensitive.
 * </p>
 * 
 * <p>
 * Implementations should make sure that all methods remain thread-safe, so that
 * token objects may be re-used in concurrent parsing sessions.
 * </p>
 */
public interface Token {

    /**
     * @return All tokens which may follow this one, in the given grammar.
     */
    List<Token> getSuccessors();

    /**
     * @param successor
     *            Token authorized as successor of the current token, in the
     *            given grammar.
     */
    void addSuccessor(Token successor);

    /**
     * This method is called by the parser, to ask the Token if a given
     * character corresponds to a valid start.
     * 
     * @param data
     *            Character to be tested, provided by the Parser
     * @return true if the Token wants to consume the character
     */
    boolean accepts(int data);

    /**
     * This is the method in charge of consuming the text stream provided by the
     * token. The Parser always calls the 'accepts' method prior to this one.
     * 
     * @param data
     *            The current character in the stream.
     * @param reader
     *            A reader to the stream.
     * @param visitor
     *            The visitor to notify when the token has consumed all the
     *            necessary chars.
     * @return The character the stream ends on, once the token has consumed all
     *         relevant chars.
     * @throws IOException
     *             When the reader cannot be accessed.
     * @throws ParseException
     *             When the initial char has been accepted, but that the next
     *             incoming chars are not expected.
     */
    int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException;

}
