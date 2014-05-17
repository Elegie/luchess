package io.elegie.luchess.web.client;

/**
 * List of all property keys authorized for a webapp configuration based on a
 * properties file.
 */
public enum WebConfigKeys {

    /**
     * Root package for controllers: "luchess.configuration.controllers.path".
     */
    PROP_KEY_CONTROLLERS_PATH("luchess.configuration.controllers.path"),

    /**
     * Simple name of the default controller, for the root URL path:
     * "luchess.configuration.controllers.default". That controller must be
     * located in the root package for all controllers.
     */
    PROP_KEY_CONTROLLERS_DEFAULT("luchess.configuration.controllers.default"),

    /**
     * Root folder for views: "luchess.configuration.views.path".
     */
    PROP_KEY_VIEWS_PATH("luchess.configuration.views.path"),

    /**
     * Root folder and prefix for message bundles:
     * "luchess.configuration.messages.path".
     */
    PROP_KEY_MESSAGES_PATH("luchess.configuration.messages.path"),

    /**
     * Root folder for source games:
     * "luchess.configuration.directory.source.path".
     */
    PROP_KEY_GAMES_PATH("luchess.configuration.directory.source.path"),

    /**
     * Password for indexing games: "luchess.configuration.indexing.password".
     */
    PROP_KEY_INDEXING_PASSWORD("luchess.configuration.indexing.password");

    private String key;

    private WebConfigKeys(String key) {
        this.key = key;
    }

    @SuppressWarnings("javadoc")
    public String getKey() {
        return key;
    }

}
