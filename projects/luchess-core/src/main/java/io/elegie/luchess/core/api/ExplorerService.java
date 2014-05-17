package io.elegie.luchess.core.api;

import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.GameListQuery;

/**
 * This service lets the client query the index for games data, using
 * {@link io.elegie.luchess.core.api.explore.Query} objects.
 */
public interface ExplorerService {

    /**
     * @return A query to list some games.
     */
    GameListQuery createGameListQuery();

    /**
     * @return A query to find a particular game.
     */
    GameFindQuery createGameFindQuery();

}
