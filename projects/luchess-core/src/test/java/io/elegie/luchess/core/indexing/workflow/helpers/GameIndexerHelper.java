package io.elegie.luchess.core.indexing.workflow.helpers;

import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.indexing.workflow.GameIndexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple indexer, that checks that every added game belongs to a set of
 * authorized games, passed to the constructor.
 */
public class GameIndexerHelper implements GameIndexer {

    private List<Game> authorizedGames = new ArrayList<>();
    private List<Game> addedGames = new ArrayList<>();

    @SuppressWarnings("javadoc")
    public GameIndexerHelper(List<Game> authorizedGames) {
        this.authorizedGames = authorizedGames;
    }

    @Override
    public synchronized void add(Game game) {
        addedGames.add(game);
    }

    /**
     * @return True if the added games are the same as the authorized ones.
     */
    public boolean gamesIntersect() {
        return authorizedGames.containsAll(addedGames) && addedGames.containsAll(authorizedGames);
    }
}
