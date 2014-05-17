package io.elegie.luchess.pgn.impl.tokens;

/**
 * An opening tag pair token starts the definition of a game metadata. The
 * character used is an opening square bracket: '['.
 * 
 * <code>
 * [foo "bar"]
 * </code>
 */
public class OpeningTagPairToken extends SingleCharToken {

    /**
     * Character to mark the opening of a tag pair: {@value #TAG_PAIR_OPENING}.
     */
    public static final char TAG_PAIR_OPENING = '[';

    @SuppressWarnings("javadoc")
    public OpeningTagPairToken() {
        super(TAG_PAIR_OPENING);
    }

}
