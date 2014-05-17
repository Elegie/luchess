package io.elegie.luchess.web.launcher.services.api;

/**
 * Represents a web application. Contains data required for a web server.
 */
@SuppressWarnings("javadoc")
public class WebApp {

    private String warPath;

    private String contextPath;

    /**
     * @param warPath
     *            The absolute path to the Jar file of the application.
     * @param contextPath
     *            The context path to configure for the application.
     */
    public WebApp(String warPath, String contextPath) {
        this.warPath = warPath;
        this.contextPath = contextPath;
    }

    public String getWarPath() {
        return warPath;
    }

    public String getContextPath() {
        return contextPath;
    }

}
