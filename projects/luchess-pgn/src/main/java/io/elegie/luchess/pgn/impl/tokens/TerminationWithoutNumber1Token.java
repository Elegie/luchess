package io.elegie.luchess.pgn.impl.tokens;

/**
 * Termination tokens for black losing ("0-1") or the game being unfinished
 * ("*").
 */
public class TerminationWithoutNumber1Token extends TerminationToken {

    @Override
    public boolean accepts(int data) {
        return data == '0' || data == '*';
    }

}
