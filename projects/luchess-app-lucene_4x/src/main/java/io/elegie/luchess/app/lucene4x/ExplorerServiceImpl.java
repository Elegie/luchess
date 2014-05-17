package io.elegie.luchess.app.lucene4x;

import io.elegie.luchess.app.lucene4x.explore.GameFindQueryImpl;
import io.elegie.luchess.app.lucene4x.explore.GameListQueryImpl;
import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.core.api.ExplorerService;
import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.GameListQuery;

/**
 * Our implementation of the explorer service. Acts as a mere factory for game
 * queries.
 */
public class ExplorerServiceImpl extends AbstractService implements ExplorerService {

    @SuppressWarnings("javadoc")
    public ExplorerServiceImpl(ServicesFactoryImpl servicesFactory) {
        super(servicesFactory);
    }

    @Override
    public GameListQuery createGameListQuery() {
        return new GameListQueryImpl(getIndexManager());
    }

    @Override
    public GameFindQuery createGameFindQuery() {
        return new GameFindQueryImpl(getIndexManager());
    }

    private IndexManager getIndexManager() {
        return getServicesFactory().getConfig().getIndexManager();
    }

}
