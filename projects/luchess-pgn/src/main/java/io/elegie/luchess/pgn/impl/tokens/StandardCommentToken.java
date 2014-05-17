package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

/**
 * Multiline comments, in the PGN format, are inserted within a pair of curly
 * brackets.
 * 
 * <code>
 * { this is a comment }
 * </code>
 */
public class StandardCommentToken extends RangeToken {

    /**
     * Character used to indicate the start of a standard comment:
     * {@link #STD_COMMENT_START}.
     */
    public static final char STD_COMMENT_START = '{';

    /**
     * Character used to indicate the start of a standard comment:
     * {@link #STD_COMMENT_END}.
     */
    public static final char STD_COMMENT_END = '}';

    @SuppressWarnings("javadoc")
    public StandardCommentToken() {
        super(STD_COMMENT_START, STD_COMMENT_END);
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitComment(value);
    }

}
