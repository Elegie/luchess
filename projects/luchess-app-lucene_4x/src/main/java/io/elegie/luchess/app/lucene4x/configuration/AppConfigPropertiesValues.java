package io.elegie.luchess.app.lucene4x.configuration;

/**
 * List of all possible values for configuration keys.
 */
@SuppressWarnings("javadoc")
public enum AppConfigPropertiesValues {

    /**
     * Index type for {@link org.apache.lucene.store.RAMDirectory}. Set value to
     * "RAM".
     */
    DIRECTORY_TYPE_RAM(AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_TYPE, "RAM"),

    /**
     * Index type for {@link org.apache.lucene.store.FSDirectory}. Set value to
     * "FS".
     */
    DIRECTORY_TYPE_FS(AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_TYPE, "FS"),

    /**
     * Update mode for the index. Set value to "CREATE" if you want the whole
     * index to be re-created each time a new indexing happens.
     */
    DIRECTORY_MODE_CREATE(AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_MODE, "CREATE"),

    /**
     * Update mode for the index. Set the value to "UPDATE" if you want to index
     * new data only, preserving existing data in the index.
     */
    DIRECTORY_MODE_UPDATE(AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_MODE, "UPDATE");

    private AppConfigPropertiesKeys key;
    private String value;

    private AppConfigPropertiesValues(AppConfigPropertiesKeys key, String value) {
        this.key = key;
        this.value = value;
    }

    public AppConfigPropertiesKeys getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
