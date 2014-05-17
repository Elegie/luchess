package io.elegie.luchess.web.framework.routing;

/**
 * A controller exception is thrown whenever a controller cannot process a web
 * request.
 */
@SuppressWarnings({ "serial", "javadoc" })
public class ControllerException extends Exception {

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

}
