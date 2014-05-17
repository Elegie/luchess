package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * All tokens extending this class should have their value as a string, such as
 * each char of the string comes from a given set (each char may be repeated).
 */
public abstract class SetToken extends AbstractToken implements DelegateVisitor {

    protected abstract boolean isInSet(int data);

    @Override
    public boolean accepts(int data) {
        return isInSet(data);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final StringBuilder valueAccumulator = new StringBuilder();
        int currentData = data;
        while (isInSet(currentData)) {
            valueAccumulator.append((char) currentData);
            currentData = reader.read();
        }
        visitToken(visitor, valueAccumulator.toString());
        return currentData;
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
    }

}
