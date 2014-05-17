package io.elegie.luchess.core.api;

import io.elegie.luchess.core.api.explore.QueryException;

/**
 * Provides the client with information regarding the index.
 */
public interface IndexInfoService {

    /**
     * @return The number of games contained in the index.
     * @throws QueryException
     *             When the index is unreachable or does not exist.
     */
    int getGamesCount() throws QueryException;

}
