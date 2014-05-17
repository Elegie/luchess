package io.elegie.luchess.web.launcher.services.api;

/**
 * Represents basic operations on a java web server.
 */
public interface ServerService {

    /**
     * Starts the server, loading the target web app. Calling this method on a
     * non-stopped server should have no effect.
     * 
     * @param webApp
     *            The web app to be loaded.
     * @throws ServerException
     *             When the server cannot be started (for instance, the address
     *             could already be in use).
     */
    void start(WebApp webApp) throws ServerException;

    /**
     * Stops a running server. Calling this method on a non-started server
     * should have no effect.
     * 
     * @throws ServerException
     *             When the server cannot be stopped.
     */
    void stop() throws ServerException;

    /**
     * @return True when the server is running.
     */
    boolean isStarted();

    /**
     * @return False when the server is not running.
     */
    boolean isStopped();

}
