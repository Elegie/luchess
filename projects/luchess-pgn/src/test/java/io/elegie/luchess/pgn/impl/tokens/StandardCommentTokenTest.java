package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.RangeWithNestingMatchTokenTest;

@SuppressWarnings("javadoc")
public class StandardCommentTokenTest extends RangeWithNestingMatchTokenTest {

    public StandardCommentTokenTest() {
        super(StandardCommentToken.class, TokenType.COMMENT, '{', '}');
    }

}
