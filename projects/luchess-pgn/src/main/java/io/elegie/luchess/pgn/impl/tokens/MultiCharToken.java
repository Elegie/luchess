package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * A token which matches any character from a predefined list.
 */
public abstract class MultiCharToken extends AbstractToken {

    private final char[] values;

    /**
     * @param values
     *            List of authorized chars.
     */
    protected MultiCharToken(char[] values) {
        this.values = new char[values.length];
        System.arraycopy(values, 0, this.values, 0, values.length);
    }

    @Override
    public boolean accepts(int data) {
        for (char value : values) {
            if (data == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        return reader.read();
    }
}
