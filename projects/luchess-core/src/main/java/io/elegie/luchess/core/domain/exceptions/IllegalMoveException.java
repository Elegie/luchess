package io.elegie.luchess.core.domain.exceptions;

/**
 * A move should be legal in a given position. This exception is thrown whenever
 * any move cannot be applied onto a position.
 */
@SuppressWarnings({ "serial", "javadoc" })
public class IllegalMoveException extends Exception {

    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException(String message, Throwable cause) {
        super(message, cause);
    }

}
