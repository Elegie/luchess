package io.elegie.luchess.pgn.impl.tokens;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides a basic implementation for a token, with a mechanism to manage
 * successors.
 */
public abstract class AbstractToken implements Token {

    protected static final String intToString(int data) {
        return Character.toString((char) data);
    }

    private final List<Token> successors = new LinkedList<>();

    @Override
    public List<Token> getSuccessors() {
        return successors;
    }

    @Override
    public void addSuccessor(Token successor) {
        successors.add(successor);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
