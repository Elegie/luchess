package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * The castle token recognizes a small or big castle.
 * 
 * <code>
 * O-O, O-O-O.
 * </code>
 */
public class CastleToken extends AbstractToken {

    /**
     * Representation for a small castle: {@value #CASTLE_SMALL}.
     */
    public static final String CASTLE_SMALL = "O-O";

    /**
     * Representation for a small castle: {@value #CASTLE_LARGE}.
     */
    public static final String CASTLE_LARGE = "O-O-O";

    /**
     * Char used to indicate a castle node: {@value #CASTLE_NODE}.
     */
    public static final char CASTLE_NODE = 'O';

    /**
     * Char used to indicate a castle node separator:
     * {@value #CASTLE_NODE_SEPARATOR}.
     */
    public static final char CASTLE_NODE_SEPARATOR = '-';

    @Override
    public boolean accepts(int data) {
        return data == CASTLE_NODE;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        int currentData = reader.read();
        if (currentData == CASTLE_NODE_SEPARATOR) {
            currentData = reader.read();
            if (currentData == CASTLE_NODE) {
                currentData = reader.read();
                if (currentData == CASTLE_NODE_SEPARATOR) {
                    currentData = reader.read();
                    if (currentData == CASTLE_NODE) {
                        visitor.visitCastle(CASTLE_LARGE);
                        return reader.read();
                    }
                } else {
                    visitor.visitCastle(CASTLE_SMALL);
                    return currentData;
                }
            }
        }
        String message = "Cannot parse Castle, encountered following char: %s (%s).";
        message = String.format(message, currentData, (char) currentData);
        throw new ParseException(message);
    }
}
