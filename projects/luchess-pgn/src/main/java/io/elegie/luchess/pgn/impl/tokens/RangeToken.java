package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * <p>
 * Some Tokens of the specification - such as Strings, Multiline Comments, or
 * Variations - are bounded by starting and ending characters. This class
 * therefore aims at providing a basic implementation, processing the data
 * contained between the boundaries.
 * </p>
 * 
 * <p>
 * Variations may contain other variations. The RangeToken class therefore takes
 * into account recursive levels, so that the appropriate ending boundary is
 * matched (and not the first one encountered).
 * </p>
 * 
 * <p>
 * Also, characters may be escaped using backward slashes. This class also
 * handles that, ignoring escaped characters.
 * </p>
 * 
 * <code>"this is a string"</code> <code>{This is a multiline \} comment}</code>
 * <code>(This is a variation (and a nested one))</code>
 */
public abstract class RangeToken extends AbstractToken implements DelegateVisitor {

    private final int start;
    private final int end;

    protected RangeToken(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean accepts(int data) {
        return this.start == data;
    }

    /**
     * The implementation makes sure that nesting is ignored when the start ==
     * end, as this would make no sense. It does so by testing the equality with
     * the end token before the start token.
     */
    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        StringBuilder valueAccumulator = new StringBuilder();

        int recursiveLevel = 0;
        while (true) {
            int currentData = reader.read();
            if (currentData == '\\') {
                valueAccumulator.append((char) currentData);
                currentData = reader.read();
            } else if (currentData == end) {
                if (recursiveLevel == 0) {
                    break;
                }
                recursiveLevel--;
            } else if (currentData == start) {
                recursiveLevel++;
            } else if (currentData == -1) {
                String message = "RangeToken is unterminated ('%s' expected).";
                message = String.format(message, (char) end);
                throw new ParseException(message);
            }
            valueAccumulator.append((char) currentData);
        }
        visitToken(visitor, valueAccumulator.toString());

        // skip the end token, as we skipped the start
        return reader.read();
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
    }

}
