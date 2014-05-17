package io.elegie.luchess.pgn.impl.tokens;

/**
 * The dot token is used to separate a move number from its corresponding value.
 * 
 * <code>
 * 1.e4
 * </code>
 */
public class Dot1Token extends SingleCharToken {

    /**
     * Character used to mark a move number-value separation: {@value #DOT}.
     */
    public static final char DOT = '.';

    @SuppressWarnings("javadoc")
    public Dot1Token() {
        super(DOT);
    }

}
