package io.elegie.luchess.core.domain.exceptions;

/**
 * When an operation is requested upon an invalid vertex.
 */
@SuppressWarnings({ "serial", "javadoc" })
public class InvalidVertexException extends Exception {

    public InvalidVertexException(String message) {
        super(message);
    }

    public InvalidVertexException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
