package io.elegie.luchess.app.lucene_4x.helpers;

import io.elegie.luchess.app.lucene4x.ServicesFactoryImpl;
import io.elegie.luchess.app.lucene4x.configuration.AppConfigPropertiesKeys;
import io.elegie.luchess.app.lucene4x.configuration.AppConfigPropertiesValues;
import io.elegie.luchess.core.api.ServicesFactory;

import java.util.Properties;

/**
 * Helpers to create and configure factories, for our tests.
 */
public final class ServicesFactoryHelper {

    private ServicesFactoryHelper() {

    }

    /**
     * Creates a new factory, with an in-memory index.
     * 
     * @param createMode
     *            Where documents should be added or updated onto the index.
     * @param moveTextLength
     *            The maximum number of moves of games to be indexed, so that we
     *            can define an appropriate analysis depth.
     * @return The properly initialized factory.
     */
    public static ServicesFactory createInMemoryIndexFactory(boolean createMode, int moveTextLength) {
        Properties parameters = new Properties();

        String keyDirectoryType = AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_TYPE.getKey();
        String valueDirectoryType = AppConfigPropertiesValues.DIRECTORY_TYPE_RAM.getValue();
        parameters.put(keyDirectoryType, valueDirectoryType);

        String keyDirectoryMode = AppConfigPropertiesKeys.PROP_KEY_DIRECTORY_MODE.getKey();
        String createDirectoryMode = AppConfigPropertiesValues.DIRECTORY_MODE_CREATE.getValue();
        String updateDirectoryMode = AppConfigPropertiesValues.DIRECTORY_MODE_UPDATE.getValue();
        String valueDirectoryMode = createMode ? createDirectoryMode : updateDirectoryMode;
        parameters.put(keyDirectoryMode, valueDirectoryMode);

        String keyAnalysisDepth = AppConfigPropertiesKeys.PROP_KEY_ANALYSIS_DEPTH.getKey();
        parameters.put(keyAnalysisDepth, (moveTextLength / 2) + 1);

        ServicesFactory factory = new ServicesFactoryImpl();
        factory.initialize(parameters);
        return factory;
    }

}
