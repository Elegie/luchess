package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * <p>
 * The end of file is reached when the reader returns -1, when its read() method
 * is called. This token does not consume chars - there is no need, as we have
 * reached the end of the stream.
 * </p>
 * 
 * <p>
 * This token is used in the special case of a file having free white spaces
 * hanging around the end of the file. We do not want to throw a game parse
 * exception, when we encounter valid tokens (spaces) but not a valid game.
 * </p>
 */
public class EndOfFileToken extends SingleCharToken {

    private static final int END_OF_STREAM = -1;

    @SuppressWarnings("javadoc")
    public EndOfFileToken() {
        super(END_OF_STREAM);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        return data;
    }

}
