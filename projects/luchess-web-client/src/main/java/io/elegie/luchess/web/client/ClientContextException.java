package io.elegie.luchess.web.client;

/**
 * Exception thrown when the client cannot provide a properly configured
 * context.
 */
@SuppressWarnings({ "serial", "javadoc" })
public class ClientContextException extends Exception {

    public ClientContextException(String message) {
        super(message);
    }

    public ClientContextException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
