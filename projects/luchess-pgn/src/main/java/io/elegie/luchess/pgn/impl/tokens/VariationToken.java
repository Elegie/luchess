package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

/**
 * A variation is a sequence of moves applicable at any point of the game. A
 * variation may be recursive, i.e. it may contain inner variations. Variations
 * should be enclosed within parentheses.
 * 
 * <code>
 * (1.e4)
 * </code>
 */
public class VariationToken extends RangeToken {

    /**
     * Character used to indicate the start of a variation:
     * {@link #VARIATION_START}.
     */
    public static final char VARIATION_START = '(';

    /**
     * Character used to indicate the start of a variation:
     * {@link #VARIATION_END}.
     */
    public static final char VARIATION_END = ')';

    @SuppressWarnings("javadoc")
    public VariationToken() {
        super(VARIATION_START, VARIATION_END);
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitVariation(value);
    }

}
