package io.elegie.luchess.app.lucene4x.configuration;

import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.core.indexing.workflow.ThreadingProfile;

/**
 * Configuration for our application. Subclasses should provide mechanisms that
 * acquire the configuration, in whatever form they like, then make it available
 * through the defined API.
 */
public interface AppConfig {

    /**
     * @return An index manager, which lets the index be read from / written to.
     */
    IndexManager getIndexManager();

    /**
     * @return The threading profile to be used when indexing.
     */
    ThreadingProfile getThreadingProfile();

    /**
     * @return The analysis depth of the opening moves.
     */
    int getAnalysisDepth();
}
