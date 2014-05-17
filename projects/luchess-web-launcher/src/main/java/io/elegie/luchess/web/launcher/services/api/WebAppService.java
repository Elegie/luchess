package io.elegie.luchess.web.launcher.services.api;

/**
 * A service providing the target web application.
 */
public interface WebAppService {

    /**
     * @return The web app to be loaded by a web server.
     */
    WebApp getWebApp();

}
