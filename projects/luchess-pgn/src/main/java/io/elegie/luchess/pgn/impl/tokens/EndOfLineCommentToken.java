package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * The PGN format allows inline comments. Such comments should be ignored by the
 * parser. An inline comment start with a semicolon, and consume all chars until
 * the end of the line.
 * 
 * The specification states that the character for the end of a line should be
 * the basic line feed (\n), but some PGN file programs use system line
 * terminators, so we must expand this definition with \r and \r\n.
 */
public class EndOfLineCommentToken extends AbstractToken {

    /**
     * The character used to indicate the start of an EOL comment:
     * {@value #EOL_COMMENT}.
     */
    public static final char EOL_COMMENT = ';';

    @Override
    public boolean accepts(int data) {
        return data == EOL_COMMENT;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final StringBuilder matchValueBuilder = new StringBuilder();
        int currentData = reader.read();
        while (currentData != -1 && currentData != '\r' && currentData != '\n') {
            matchValueBuilder.append((char) currentData);
            currentData = reader.read();
        }
        if (currentData != -1) {
            do {
                currentData = reader.read();
            } while (currentData != -1 && currentData == '\n');
        }
        visitor.visitComment(matchValueBuilder.toString());
        return currentData;
    }

}
