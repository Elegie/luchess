package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * The capture is recognized when a 'x' char is matched. The token matches only
 * the capture part, i.e. the 'x'.
 * 
 * <code>
 * exd4, Nxf3.
 * </code>
 */
public class CaptureToken extends SingleCharToken {

    /**
     * Character used to indicate a capture: {@value #CAPTURE}.
     */
    public static final char CAPTURE = 'x';

    @SuppressWarnings("javadoc")
    public CaptureToken() {
        super(CAPTURE);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitCapture(Character.toString(CAPTURE));
        return currentData;
    }
}
