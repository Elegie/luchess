package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

@SuppressWarnings("javadoc")
public class OpeningTagPairTokenTest extends OneMatchTokenTest {

    public OpeningTagPairTokenTest() {
        super(OpeningTagPairToken.class, null, '[');
    }

}
