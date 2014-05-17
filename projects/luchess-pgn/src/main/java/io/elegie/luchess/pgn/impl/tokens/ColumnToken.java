package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * Possible board columns, in chess, range from 'a' to 'h', always in lower
 * case.
 * 
 * <code>
 * e4 ('e' is the column).
 * </code>
 */
public class ColumnToken extends MultiCharToken {

    /**
     * List of characters used to represent a column: {@value #COLS}.
     */
    static final char[] COLS = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

    @SuppressWarnings("javadoc")
    public ColumnToken() {
        super(COLS);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitColumn(intToString(data));
        return currentData;
    }
}
