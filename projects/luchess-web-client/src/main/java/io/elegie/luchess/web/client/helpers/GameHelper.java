package io.elegie.luchess.web.client.helpers;

import io.elegie.luchess.core.api.ExplorerService;
import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.web.client.ClientContext;

/**
 * This class lets us retrieve a particular game, given a game ID.
 */
public final class GameHelper {

    private GameHelper() {

    }

    /**
     * @param id
     *            The id of the game to be retrieved.
     * @return The game if found, with its moves generated, or null if not
     *         found.
     * @throws QueryException
     *             When the service cannot be reached.
     */
    public static Game getGame(String id) throws QueryException {
        ExplorerService explorer = ClientContext.INSTANCE.getServicesFactory().getExplorerService();
        GameFindQuery query = explorer.createGameFindQuery();
        query.setId(id);
        return query.execute().getGame();
    }
}
