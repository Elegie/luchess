package io.elegie.luchess.core.api.explore;

import io.elegie.luchess.core.domain.entities.Game;

/**
 * The result of the query, which may contain the searched game.
 */
public interface GameFindQueryResult extends QueryResult {

    /**
     * @return The game matching the query, or null if no game was found.
     */
    Game getGame();

}
