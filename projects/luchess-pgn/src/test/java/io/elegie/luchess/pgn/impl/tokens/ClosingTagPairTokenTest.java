package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

@SuppressWarnings("javadoc")
public class ClosingTagPairTokenTest extends OneMatchTokenTest {

    public ClosingTagPairTokenTest() {
        super(ClosingTagPairToken.class, null, ']');
    }

}
