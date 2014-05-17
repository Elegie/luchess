package io.elegie.luchess.app.lucene4x.configuration;

import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.FSIndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.RAMIndexManager;
import io.elegie.luchess.core.indexing.workflow.ThreadingProfile;

import java.io.File;
import java.util.Properties;

/**
 * This is the configuration used by the
 * {@link io.elegie.luchess.core.api.ServicesFactory}, to configure each
 * service.
 */
public class AppConfigPropertiesImpl implements AppConfig {

    private IndexManager indexManager;
    private ThreadingProfile profile;
    private int analysisDepth;

    /**
     * @param parameters
     *            The properties from which the configuration should be built.
     */
    public AppConfigPropertiesImpl(Properties parameters) {
        createAnalysisDepth(parameters);
        createThreadingProfile(parameters);
        createIndexManager(parameters);
    }

    // --- Analysis Depth -----------------------------------------------------

    private void createAnalysisDepth(Properties parameters) {
        analysisDepth = readPositiveInt(parameters, AppConfigPropertiesKeys.PROP_KEY_ANALYSIS_DEPTH);
    }

    // --- Threading Profile --------------------------------------------------

    private void createThreadingProfile(Properties parameters) {
        profile = new ThreadingProfile();
        profile.setNumberOfThreads(readPositiveInt(parameters, AppConfigPropertiesKeys.PROP_KEY_PROFILE_THREADS_COUNT));
        profile.setTimeoutSeconds(readPositiveInt(parameters, AppConfigPropertiesKeys.PROP_KEY_PROFILE_TIMEOUT_SECONDS));
    }

    // --- IndexManager -------------------------------------------------------

    private void createIndexManager(Properties parameters) {
        boolean openModeCreate = readOpenModeCreate(parameters);
        String valueDirectoryType = readString(parameters, AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_TYPE);
        if (AppConfigPropertiesValues.DIRECTORY_TYPE_RAM.getValue().equalsIgnoreCase(valueDirectoryType)) {
            indexManager = new RAMIndexManager(analysisDepth, openModeCreate);
        } else if (AppConfigPropertiesValues.DIRECTORY_TYPE_FS.getValue().equalsIgnoreCase(valueDirectoryType)) {
            String valueDirectoryPath = readString(parameters, AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_FS_PATH);
            File target = readPath(valueDirectoryPath);
            indexManager = new FSIndexManager(analysisDepth, openModeCreate, target);
        } else {
            String message = "Unrecognized directory type: %s.";
            message = String.format(message, valueDirectoryType);
            throw new IllegalArgumentException(message);
        }
    }

    private boolean readOpenModeCreate(Properties parameters) {
        String valueOpenMode = readString(parameters, AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_MODE);
        if (AppConfigPropertiesValues.DIRECTORY_MODE_CREATE.getValue().equalsIgnoreCase(valueOpenMode)) {
            return true;
        } else if (AppConfigPropertiesValues.DIRECTORY_MODE_UPDATE.getValue().equalsIgnoreCase(valueOpenMode)) {
            return false;
        } else {
            String message = "Unrecognized directory open-mode: " + valueOpenMode;
            throw new IllegalArgumentException(message);
        }
    }

    private File readPath(String path) {
        File target = new File(path);
        if (!target.exists() || !target.canRead() || !target.canWrite()) {
            String message = "The path for the index does not exist, or is not readable/writable: " + path;
            throw new IllegalArgumentException(message);
        }
        return target;
    }

    // --- Helpers ------------------------------------------------------------

    private static String readString(Properties parameters, AppConfigPropertiesKeys propertyKey) {
        String key = propertyKey.getKey();
        Object value = parameters.get(key);
        if (value == null) {
            value = propertyKey.getDefaultValue();
        }
        if (value == null) {
            String message = "Missing property: " + key;
            throw new IllegalArgumentException(message);
        }
        return value.toString();
    }

    private static int readPositiveInt(Properties parameters, AppConfigPropertiesKeys propertyKey) {
        String value = readString(parameters, propertyKey);
        try {
            int intValue = Integer.parseInt(value);
            if (intValue > 0) {
                return intValue;
            }
            String message = "The int value (%s) for property %s must be positive.";
            message = String.format(message, value, propertyKey);
            throw new IllegalArgumentException(message);
        } catch (NumberFormatException e) {
            String message = "The value (%s) for property %s must be a positive integer.";
            message = String.format(message, value, propertyKey);
            throw new IllegalArgumentException(message, e);
        }
    }

    // ------------------------------------------------------------------------

    @Override
    public IndexManager getIndexManager() {
        return indexManager;
    }

    @Override
    public ThreadingProfile getThreadingProfile() {
        return profile;
    }

    @Override
    public int getAnalysisDepth() {
        return analysisDepth;
    }

}
