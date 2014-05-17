package io.elegie.luchess.web.client.helpers;

import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class TimedGameListQueryHelper implements GameListQuery {

    public static final String BLACK = "Black";
    public static final String WHITE = "White";
    public static final List<String> MOVES = new ArrayList<>();
    public static final int ELO = 2500;
    public static final int PAGE_COUNT = 10;
    public static final int PAGE_START = 1;

    private static final int TOTAL = 6;

    private int count = 0;
    private boolean executed = false;

    @Override
    public GameListQueryResult execute() throws QueryException {
        executed = true;
        return new TimedGameListQueryResultHelper();
    }

    @Override
    public void setWhite(String white) {
        count += WHITE.equals(white) ? +1 : -1;
    }

    @Override
    public void setBlack(String black) {
        count += BLACK.equals(black) ? +1 : -1;
    }

    @Override
    public void setMinElo(int elo) {
        count += ELO == elo ? +1 : -1;
    }

    @Override
    public void setMoves(List<String> moves) {
        count += MOVES.equals(moves) ? +1 : -1;
    }

    @Override
    public void setPageStart(int pageStart) {
        count += PAGE_START == pageStart ? +1 : -1;
    }

    @Override
    public void setPageCount(int pageCount) {
        count += PAGE_COUNT == pageCount ? +1 : -1;
    }

    public boolean verifyAllParametersSet() {
        return count == TOTAL;
    }

    public boolean verifyExecuted() {
        return executed;
    }

}
