package io.elegie.luchess.core.domain.exceptions;

/**
 * Thrown when the deserialization of a position cannot be performed.
 */
@SuppressWarnings({ "serial", "javadoc" })
public class InvalidPositionException extends Exception {

    public InvalidPositionException(String message) {
        super(message);
    }

    public InvalidPositionException(String message, Throwable cause) {
        super(message, cause);
    }

}
