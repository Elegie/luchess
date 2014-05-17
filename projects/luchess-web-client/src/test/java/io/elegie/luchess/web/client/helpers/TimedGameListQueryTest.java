package io.elegie.luchess.web.client.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class TimedGameListQueryTest {

    @Test
    public void testQuery() throws QueryException {
        TimedGameListQueryHelper inner = new TimedGameListQueryHelper();
        TimedGameListQuery outer = new TimedGameListQuery(inner);
        outer.setBlack(TimedGameListQueryHelper.BLACK);
        outer.setMinElo(TimedGameListQueryHelper.ELO);
        outer.setMoves(TimedGameListQueryHelper.MOVES);
        outer.setPageCount(TimedGameListQueryHelper.PAGE_COUNT);
        outer.setPageStart(TimedGameListQueryHelper.PAGE_START);
        outer.setWhite(TimedGameListQueryHelper.WHITE);
        assertTrue(inner.verifyAllParametersSet());
        assertFalse(inner.verifyExecuted());
        GameListQueryResult result = outer.execute();
        assertTrue(inner.verifyExecuted());
        assertEquals(TimedGameListQueryResultHelper.GAMES, result.getGames());
        assertEquals(TimedGameListQueryResultHelper.CONTINUATIONS, result.getContinuations());
        assertEquals(TimedGameListQueryResultHelper.TOTAL_WHITE_WINS, result.getTotalWhiteWins());
        assertEquals(TimedGameListQueryResultHelper.TOTAL_BLACK_WINS, result.getTotalBlackWins());
        assertEquals(TimedGameListQueryResultHelper.TOTAL_DRAWS, result.getTotalDraws());
        assertEquals(TimedGameListQueryResultHelper.TOTAL_UNFINISHED, result.getTotalUnfinished());
    }
}
