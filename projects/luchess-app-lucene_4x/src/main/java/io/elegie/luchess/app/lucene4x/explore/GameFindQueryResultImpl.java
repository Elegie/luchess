package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.core.api.explore.GameFindQueryResult;
import io.elegie.luchess.core.domain.entities.Game;

/**
 * A simple implementation of the {@link GameFindQueryResult}, wrapping the
 * sought game.
 */
public class GameFindQueryResultImpl implements GameFindQueryResult {

    private Game game;

    /**
     * @param game
     *            The result of the query. May be null if no game was found.
     */
    public GameFindQueryResultImpl(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {game: %s}";
        return String.format(value, className, game);
    }

    @Override
    public Game getGame() {
        return game;
    }

}
