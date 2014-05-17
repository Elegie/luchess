package io.elegie.luchess.pgn.impl.tokens;

/**
 * This matches any sort of white space.
 */
public class SpaceSeparatorToken extends SetToken {

    @Override
    protected boolean isInSet(int data) {
        return data == ' ' || data == '\t' || data == '\r' || data == '\n';
    }

}
