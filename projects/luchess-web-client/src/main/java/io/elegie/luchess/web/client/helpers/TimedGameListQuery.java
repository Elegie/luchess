package io.elegie.luchess.web.client.helpers;

import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;

import java.util.List;

/**
 * Query decorator, used to calculate the execution duration of any wrapped
 * query.
 */
public class TimedGameListQuery implements GameListQuery {

    private GameListQuery decorated;

    /**
     * @param decorated
     *            The query for which we want to calculate the execution
     *            duration.
     */
    public TimedGameListQuery(GameListQuery decorated) {
        this.decorated = decorated;
    }

    @Override
    public GameListQueryResult execute() throws QueryException {
        long start = System.currentTimeMillis();
        GameListQueryResult decoratedResult = decorated.execute();
        long end = System.currentTimeMillis();

        TimedGameListQueryResult result = new TimedGameListQueryResult(decoratedResult);
        result.setDuration(end - start);
        return result;
    }

    @Override
    public void setWhite(String name) {
        decorated.setWhite(name);
    }

    @Override
    public void setBlack(String name) {
        decorated.setBlack(name);
    }

    @Override
    public void setMinElo(int elo) {
        decorated.setMinElo(elo);
    }

    @Override
    public void setMoves(List<String> moves) {
        decorated.setMoves(moves);
    }

    @Override
    public void setPageStart(int pageStart) {
        decorated.setPageStart(pageStart);
    }

    @Override
    public void setPageCount(int pageCount) {
        decorated.setPageCount(pageCount);
    }

}
