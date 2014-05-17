package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * The check token indicates that the opponent king is threatened. Two checks
 * are possible: king check ('+') and checkmate ('#').
 */
public class CheckToken extends MultiCharToken {

    /**
     * Authorized characters to indicate a king check (normal check and
     * checkmate): {@value #VALUES}.
     */
    static final char[] VALUES = new char[] { '+', '#' };

    @SuppressWarnings("javadoc")
    public CheckToken() {
        super(VALUES);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitCheck(intToString(data));
        return currentData;
    }
}
