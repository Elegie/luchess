package io.elegie.luchess.web.client.helpers;

import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Game;

import java.util.List;

/**
 * A query result decorator, which includes information about the execution
 * duration of a query wrapped by a {@link TimedGameListQuery}.
 */
@SuppressWarnings("javadoc")
public class TimedGameListQueryResult implements GameListQueryResult {

    private GameListQueryResult decorated;
    private long duration;

    /**
     * @param decorated
     *            The result of the decorated query.
     */
    public TimedGameListQueryResult(GameListQueryResult decorated) {
        this.decorated = decorated;
    }

    @Override
    public List<Game> getGames() {
        return decorated.getGames();
    }

    @Override
    public List<Continuation> getContinuations() {
        return decorated.getContinuations();
    }

    @Override
    public int getTotalWhiteWins() {
        return decorated.getTotalWhiteWins();
    }

    @Override
    public int getTotalBlackWins() {
        return decorated.getTotalBlackWins();
    }

    @Override
    public int getTotalDraws() {
        return decorated.getTotalDraws();
    }

    @Override
    public int getTotalUnfinished() {
        return decorated.getTotalUnfinished();
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
