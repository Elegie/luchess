package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.RangeNoNestingMatchTokenTest;

@SuppressWarnings("javadoc")
public class TagPairValueTokenTest extends RangeNoNestingMatchTokenTest {

    public TagPairValueTokenTest() {
        super(TagPairValueToken.class, TokenType.TAG_PAIR_VALUE, '"', '"');
    }

}
