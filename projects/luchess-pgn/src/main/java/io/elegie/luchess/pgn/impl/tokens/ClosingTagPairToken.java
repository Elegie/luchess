package io.elegie.luchess.pgn.impl.tokens;

/**
 * A tag pair ends with a right square bracket.
 * 
 * <code>
 * [foo "bar"]
 * </code>
 */
public class ClosingTagPairToken extends SingleCharToken {

    /**
     * Character to mark the closing of a tag pair: {@value #TAG_PAIR_CLOSING}.
     */
    public static final char TAG_PAIR_CLOSING = ']';

    @SuppressWarnings("javadoc")
    public ClosingTagPairToken() {
        super(TAG_PAIR_CLOSING);
    }

}
