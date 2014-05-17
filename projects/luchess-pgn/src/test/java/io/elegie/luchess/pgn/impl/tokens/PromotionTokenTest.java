package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

@SuppressWarnings("javadoc")
public class PromotionTokenTest extends OneMatchTokenTest {

    public PromotionTokenTest() {
        super(PromotionToken.class, TokenType.PROMOTION, '=');
    }

}
