package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * A NAG Token refers to a Numeric Annotation Glyph, i.e. some entity which
 * qualifies the move (for instance: "good move!"). From a specification point
 * of view, a NAG starts with a dollar sign and is followed by a number, but
 * actual annotations also include any kind of characters, which are not treated
 * here. I have seen no formal description for these, so am a bit embarrassed
 * about how to feed them correctly.
 * 
 * <code>
 * $42
 * </code>
 */
public class NAGToken extends NumberToken {

    /**
     * Character used to indicate the start of a NAG: {@value #NAG}.
     */
    public static final char NAG = '$';

    @Override
    public boolean accepts(int data) {
        return data == NAG;
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = reader.read();
        if (super.accepts(currentData)) {
            return super.interpret(currentData, reader, visitor);
        }
        String message = "Invalid NAG Token. Expected number, but encountered '%s'.";
        message = String.format(message, (char) currentData);
        throw new ParseException(message);
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitNAG(Character.toString(NAG) + value);
    }
}
