package io.elegie.luchess.web.framework.exceptions;

/**
 * Thrown when the request flow cannot be processed. Usually, this exception is
 * bubbled up to the front servlet, to be wrapped and thrown as a servlet
 * exception.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class ApplicationException extends Exception {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
