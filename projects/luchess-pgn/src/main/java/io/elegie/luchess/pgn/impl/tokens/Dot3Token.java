package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * A move may be split by various inserts, such as a comment, a NAG value, a
 * variation. When the second move resumes, then the notation implies that some
 * 3-dot placeholder be set.
 * 
 * <code>
 * 1.e4 {comment} 1...e5
 * </code>
 */
public class Dot3Token extends AbstractToken {

    @Override
    public boolean accepts(int data) {
        return data == Dot1Token.DOT;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        int currentData = reader.read();
        if (currentData == Dot1Token.DOT) {
            currentData = reader.read();
            if (currentData == Dot1Token.DOT) {
                return reader.read();
            }
        }
        String message = "Dot3Token should be 3 dots, not '%s'.";
        message = String.format(message, (char) currentData);
        throw new ParseException(message);

    }
}
