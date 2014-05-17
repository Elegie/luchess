package io.elegie.luchess.pgn.impl.tokens;

/**
 * In the specification, the digit '1' may be used to either describe a move
 * number, or a game termination. Having the same character for the start of two
 * different tokens is a bit of a pain, as it requires a custom treatment.
 */
public class Number1Token extends SingleCharToken {

    @SuppressWarnings("javadoc")
    public Number1Token() {
        super('1');
    }

}
