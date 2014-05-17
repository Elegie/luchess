package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * The root token serves as a start token for a tree, or subtree.
 */
public class RootToken extends AbstractToken {

    @Override
    public boolean accepts(int data) {
        return true;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        return (data == 0) ? reader.read() : data;
    }
}
