package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple POJO implementation for the #{@link GameListQueryResult}.
 */
@SuppressWarnings("javadoc")
public class GameListQueryResultImpl implements GameListQueryResult {

    private List<Game> games = new ArrayList<>();
    private List<Continuation> continuations = new ArrayList<>();
    private int totalWhiteWins;
    private int totalBlackWins;
    private int totalDraws;
    private int totalUnfinished;

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {totalWhiteWins: %s, totalBlackWins: %s, totalDraws: %s, totalUnfinished: %s, games: %s, continuations: %s}";
        return String.format(value, className, totalWhiteWins, totalBlackWins, totalDraws, totalUnfinished, games,
                continuations);
    }

    @Override
    public List<Game> getGames() {
        return games;
    }

    @Override
    public List<Continuation> getContinuations() {
        return continuations;
    }

    @Override
    public int getTotalWhiteWins() {
        return totalWhiteWins;
    }

    @Override
    public int getTotalBlackWins() {
        return totalBlackWins;
    }

    @Override
    public int getTotalDraws() {
        return totalDraws;
    }

    @Override
    public int getTotalUnfinished() {
        return totalUnfinished;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void setContinuations(List<Continuation> continuations) {
        this.continuations = continuations;
    }

    public void setTotalWhiteWins(int totalWhiteWins) {
        this.totalWhiteWins = totalWhiteWins;
    }

    public void setTotalBlackWins(int totalBlackWins) {
        this.totalBlackWins = totalBlackWins;
    }

    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
    }

    public void setTotalUnfinished(int totalUnfinished) {
        this.totalUnfinished = totalUnfinished;
    }

}
