package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.RangeWithNestingMatchTokenTest;

@SuppressWarnings("javadoc")
public class VariationTokenTest extends RangeWithNestingMatchTokenTest {

    public VariationTokenTest() {
        super(VariationToken.class, TokenType.VARIATION, '(', ')');
    }

}
