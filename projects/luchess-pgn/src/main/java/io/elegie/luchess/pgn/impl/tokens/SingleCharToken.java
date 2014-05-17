package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * This class defines a token matching exactly one char, which should be
 * configured by subclasses in their constructor call.
 */
public abstract class SingleCharToken extends AbstractToken {

    private final int charToken;

    protected SingleCharToken(int charToken) {
        this.charToken = charToken;
    }

    @Override
    public boolean accepts(int data) {
        return data == charToken;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        return reader.read();
    }

}
