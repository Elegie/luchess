package io.elegie.luchess.app.lucene4x.configuration;

/**
 * List of all property keys authorized for a services configuration based on a
 * properties file.
 * 
 * @see AppConfigPropertiesValues
 */
@SuppressWarnings("javadoc")
public enum AppConfigPropertiesKeys {

    /**
     * FQN of the services factory: "luchess.configuration.services.factory".
     */
    PROP_KEY_FACTORY("luchess.configuration.services.factory", null),

    /**
     * Type of the Lucene directory: "luchess.configuration.directory.type".
     */
    PROP_KEY_DIRECTORY_TYPE("luchess.configuration.directory.type", null),

    /**
     * Path of the index directory, when the directory is a file system -based
     * one: "luchess.configuration.directory.target.path".
     */
    PROP_KEY_DIRECTORY_FS_PATH("luchess.configuration.directory.target.path", null),

    /**
     * Update mode of the directory: "luchess.configuration.directory.mode".
     */
    PROP_KEY_DIRECTORY_MODE("luchess.configuration.directory.mode", null),

    /**
     * Key for the number of threads to be used while indexing:
     * "luchess.configuration.profile.threads.count".
     */
    PROP_KEY_PROFILE_THREADS_COUNT("luchess.configuration.profile.threads.count",
            Integer.toString(Runtime.getRuntime().availableProcessors())),

    /**
     * Key for the build timeout, i.e. after how much time should the indexing
     * session be interrupted: "luchess.configuration.profile.timeout.seconds".
     * This value is expressed in seconds.
     */
    PROP_KEY_PROFILE_TIMEOUT_SECONDS("luchess.configuration.profile.timeout.seconds", "3600"),

    /**
     * Key for the index analysis depth, i.e. the opening moves depth:
     * "luchess.configuration.analysis.depth".
     * 
     * Moves will be indexed only up to the analysis depth, which means that
     * fast search and continuations will be available only up to that
     * threshold. The higher the analysis depth, the bigger the index, the
     * slower the indexing, the faster the search.
     * 
     * Searches beyond the analysis depth remain possible, but will be slower
     * and will not offer continuations.
     * 
     * The default analysis depth should be set to the average length of common
     * chess openings.
     * 
     * @see io.elegie.luchess.core.domain.entities.Continuation
     */
    PROP_KEY_ANALYSIS_DEPTH("luchess.configuration.analysis.depth", "20");

    private String key;
    private String defaultValue;

    private AppConfigPropertiesKeys(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
