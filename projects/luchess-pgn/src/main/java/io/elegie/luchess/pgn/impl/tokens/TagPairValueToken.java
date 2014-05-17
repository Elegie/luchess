package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

/**
 * Tag pairs are key/value pairs representing a game metadata. The value of the
 * pair is always a string, can be anything, but must be wrapped within string
 * delimiters (double quote).
 * 
 * <code>
 * [foo "bar"]
 * </code>
 */
@SuppressWarnings("javadoc")
public class TagPairValueToken extends RangeToken {

    /**
     * Character used to indicate the start and the end of a tag pair value:
     * {@link #TAG_PAIR_VALUE_DELIMITOR}.
     */
    public static final char TAG_PAIR_VALUE_DELIMITOR = '"';

    public TagPairValueToken() {
        super(TAG_PAIR_VALUE_DELIMITOR, TAG_PAIR_VALUE_DELIMITOR);
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitTagPairValue(value);
    }
}
