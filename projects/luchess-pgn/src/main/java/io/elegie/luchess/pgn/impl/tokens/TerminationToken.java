package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * <p>
 * A Termination token marks the end of a game. It should be equal to the value
 * of the tag "Result". Possible outcomes include {@value #WHITE_WINS},
 * {@value #BLACK_WINS}, {@value #DRAW}, and {@value #UNFINISHED} (an unfinished
 * game).
 * </p>
 * 
 * <p>
 * Some termination tokens start with the number 1, which may be conflicting
 * with other tokens (such as a move number). We need a convention. All
 * termination tokens starting with the number 1 shall in fact be implemented
 * with two separate tokens: the first part being the number 1 (using
 * {@link io.elegie.luchess.pgn.impl.tokens.Number1Token}) and the second part
 * being the remainder of the token (
 * {@link io.elegie.luchess.pgn.impl.tokens.TerminationWithNumber1Token} ).
 * Other termination tokens are implemented with a single token class (
 * {@link io.elegie.luchess.pgn.impl.tokens.TerminationWithoutNumber1Token} ).
 * </p>
 */
public abstract class TerminationToken extends AbstractToken {

    /**
     * Termination for white winning: {@value #WHITE_WINS}
     */
    public static final String WHITE_WINS = "1-0";

    /**
     * Termination for black winning: {@value #BLACK_WINS}
     */
    public static final String BLACK_WINS = "0-1";

    /**
     * Termination for a draw: {@value #DRAW}
     */
    public static final String DRAW = "1/2-1/2";

    /**
     * Termination for an unfinished game: {@value #UNFINISHED}
     */
    public static final String UNFINISHED = "*";

    private static final int[] WHITE_WINS_PARTS = new int[] { '-', '0' };
    private static final int[] BLACK_WINS_PARTS = new int[] { '0', '-', '1' };
    private static final int[] DRAW_PARTS = new int[] { '/', '2', '-', '1', '/', '2' };

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        int currentData = data;
        String value = "";
        switch (data) {
            case '*':
                value = UNFINISHED;
                break;
            case '-':
                value = WHITE_WINS;
                currentData = readSequence(data, reader, WHITE_WINS_PARTS);
                break;
            case '0':
                value = BLACK_WINS;
                currentData = readSequence(data, reader, BLACK_WINS_PARTS);
                break;
            case '/':
                value = DRAW;
                currentData = readSequence(data, reader, DRAW_PARTS);
                break;
            default:
                throwParseException(data);
        }
        visitor.visitTermination(value);
        return currentData;
    }

    private int readSequence(int data, Reader reader, int[] sequence) throws ParseException, IOException {
        int currentData = data;
        for (int ii = 1; ii < sequence.length; ii++) {
            currentData = reader.read();
            if (sequence[ii] != currentData) {
                throwParseException(data);
            }
        }
        return currentData;
    }

    private void throwParseException(int data) throws ParseException {
        String message = "Cannot parse termination token (%s)";
        message = String.format(message, (char) data);
        throw new ParseException(message);
    }
}
