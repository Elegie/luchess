package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.TokenType;
import io.elegie.luchess.pgn.impl.tokens.helpers.OneMatchTokenTest;

@SuppressWarnings("javadoc")
public class CaptureTokenTest extends OneMatchTokenTest {

    public CaptureTokenTest() {
        super(CaptureToken.class, TokenType.CAPTURE, 'x');
    }

}
