package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * This token is used to mark the beginning of the moves of the game, right
 * after the metadata section. It does not consume any char from the string, as
 * it matches the string '1', which is also the first move of the game (and
 * should therefore be properly matched right after).
 */
public class MoveTextStartToken extends AbstractToken {

    /**
     * Character used to indicate the start of the move text: {@value #START}.
     */
    public static final char START = '1';

    @Override
    public boolean accepts(int data) {
        return data == START;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        return data;
    }

}
