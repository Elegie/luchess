package io.elegie.luchess.core.api.explore;

import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Game;

import java.util.List;

/**
 * Returns the page of games, matching the criteria specified in the query.
 * Statistical data about possible continuations are also included.
 */
public interface GameListQueryResult extends QueryResult {

    /**
     * @return List of games matching the query, contained in the current page.
     */
    List<Game> getGames();

    /**
     * @return Continuations for all games matching the query.
     */
    List<Continuation> getContinuations();

    /**
     * @return Number of victories by White.
     */
    int getTotalWhiteWins();

    /**
     * @return Number of victories by Black.
     */
    int getTotalBlackWins();

    /**
     * @return Number of drawn games.
     */
    int getTotalDraws();

    /**
     * @return Number of unfinished games.
     */
    int getTotalUnfinished();

}
