package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * This token matches a chess figurine, such as King, Queen, Bishop, Knight and
 * Rook. Note that pawns are not recognized as figurines.
 * 
 * <code>
 * Re1, Nf3+, Bxf7, Qe4#, Kg1, e8=Q
 * </code>
 */
public class FigurineToken extends MultiCharToken {

    /**
     * List of characters used to represent a figurine: {@value #FIGURINES}.
     */
    static final char[] FIGURINES = new char[] { 'R', 'N', 'B', 'Q', 'K' };

    @SuppressWarnings("javadoc")
    public FigurineToken() {
        super(FIGURINES);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitFigurine(intToString(data));
        return currentData;
    }
}
