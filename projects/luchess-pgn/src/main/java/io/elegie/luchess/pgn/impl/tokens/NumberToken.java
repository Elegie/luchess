package io.elegie.luchess.pgn.impl.tokens;

/**
 * This token basically matches any number. It is used to qualify a move number,
 * such as "14.e7".
 */
public class NumberToken extends SetToken {

    @Override
    protected boolean isInSet(int data) {
        return data >= '0' && data <= '9';
    }

}
