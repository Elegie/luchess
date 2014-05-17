package io.elegie.luchess.web.launcher.services.api;

/**
 * Exception thrown when invoking a server operation fails (start, stop).
 */
@SuppressWarnings({ "javadoc", "serial" })
public class ServerException extends Exception {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }

    public ServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
