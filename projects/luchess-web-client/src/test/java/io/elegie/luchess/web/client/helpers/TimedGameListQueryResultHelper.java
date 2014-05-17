package io.elegie.luchess.web.client.helpers;

import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Game;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class TimedGameListQueryResultHelper implements GameListQueryResult {

    public static final List<Game> GAMES = new ArrayList<>();
    public static final List<Continuation> CONTINUATIONS = new ArrayList<>();
    public static final int TOTAL_WHITE_WINS = 1;
    public static final int TOTAL_BLACK_WINS = 2;
    public static final int TOTAL_DRAWS = 3;
    public static final int TOTAL_UNFINISHED = 4;

    @Override
    public List<Game> getGames() {
        return GAMES;
    }

    @Override
    public List<Continuation> getContinuations() {
        return CONTINUATIONS;
    }

    @Override
    public int getTotalWhiteWins() {
        return TOTAL_WHITE_WINS;
    }

    @Override
    public int getTotalBlackWins() {
        return TOTAL_BLACK_WINS;
    }

    @Override
    public int getTotalDraws() {
        return TOTAL_DRAWS;
    }

    @Override
    public int getTotalUnfinished() {
        return TOTAL_UNFINISHED;
    }

}
