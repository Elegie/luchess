package io.elegie.luchess.pgn.impl.tokens;

/**
 * The second part of a termination token, starting by the number 1. We can have
 * a white victory ("1-0") or a draw ("1/2-1/2").
 */
public class TerminationWithNumber1Token extends TerminationToken {

    @Override
    public boolean accepts(int data) {
        return data == '-' || data == '/';
    }

}
