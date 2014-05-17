package io.elegie.luchess.core.api;

import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataSet;

/**
 * The Builder service is used to construct an index.
 */
public interface BuilderService {

    /**
     * Requests an index construction. Implementations are encouraged to use a
     * new thread for the construction, as indexing may take many minutes,
     * depending on the volume of data to be indexed.
     * 
     * @param dataSet
     *            Set of game data to be indexed.
     * 
     * @param monitor
     *            Implementations should provide feedback on the construction,
     *            using a dedicated build monitor (a visitor), so that clients
     *            may report the construction progress as needed.
     * 
     * @throws BuildException
     *             When the process fails.
     */
    void index(SourceDataSet dataSet, IndexMonitor monitor) throws BuildException;

}
