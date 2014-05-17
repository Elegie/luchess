package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * Possible board rows, in chess, range from '1' to '8'.
 * 
 * <code>
 * e4 ('4' is the row).
 * </code>
 */
public class RowToken extends MultiCharToken {

    /**
     * List of characters used to represent a row: {@value #ROWS}.
     */
    static final char[] ROWS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8' };

    @SuppressWarnings("javadoc")
    public RowToken() {
        super(ROWS);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitRow(intToString(data));
        return currentData;
    }

}
