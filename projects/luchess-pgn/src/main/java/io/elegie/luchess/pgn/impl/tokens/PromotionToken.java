package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.ParserVisitor;

import java.io.IOException;
import java.io.Reader;

/**
 * This token indicates a pawn promotion, e.g. "e8=Q" - it matches the '=' sign.
 */
public class PromotionToken extends SingleCharToken {

    /**
     * Character used to indicate an upcoming promoted figurine:
     * {@value #PROMOTION}.
     */
    public static final char PROMOTION = '=';

    @SuppressWarnings("javadoc")
    public PromotionToken() {
        super(PROMOTION);
    }

    @Override
    public int interpret(int data, Reader reader, ParserVisitor visitor) throws IOException, ParseException {
        final int currentData = super.interpret(data, reader, visitor);
        visitor.visitPromotion(Character.toString(PROMOTION));
        return currentData;
    }
}
