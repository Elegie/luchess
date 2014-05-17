package io.elegie.luchess.core.indexing.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.api.helpers.GameHelper;
import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.moves.KnightMove;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;
import io.elegie.luchess.core.indexing.workflow.helpers.GameIndexerHelper;
import io.elegie.luchess.core.indexing.workflow.helpers.IndexMonitorHelper;
import io.elegie.luchess.core.indexing.workflow.helpers.SimpleSourceDataSet;
import io.elegie.luchess.core.indexing.workflow.helpers.SingleGameSourceDataUnit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Checks that the threaded indexing session utility works fine, using at least
 * two threads.
 */
@SuppressWarnings("javadoc")
public class IndexingSessionTest {

    private static final PawnForwardMove E4 = new PawnForwardMove();
    private static final KnightMove NF3 = new KnightMove();

    private static final int NUM_GAMES = 2;
    private static final int TIMEOUT_SECONDS = 5;

    static {
        E4.setTarget(new Vertex('e', 4));
        NF3.setTarget(new Vertex('f', 3));
    }

    private IndexingSession session;

    /**
     * Create and initialize a basic indexing session. Tests may override the
     * session in order to check for given tests.
     */
    @Before
    public void setUp() {
        List<Game> games = new ArrayList<>();
        games.add(createGame(E4));
        games.add(createGame(NF3));

        session = new IndexingSession();
        session.setDataSet(createSourceDataSet(games));
        session.setIndexer(createIndexer(games));
        session.setMonitor(createMonitor());
        session.setProfile(createThreadingProfile());
    }

    private Game createGame(Move move) {
        Game game = GameHelper.createNewGame(1, move.getValue());
        game.getMoveText().addValue(move.getValue());
        return game;
    }

    private SimpleSourceDataSet createSourceDataSet(List<Game> games) {
        List<SingleGameSourceDataUnit> dataUnits = new ArrayList<>();
        for (Game game : games) {
            dataUnits.add(new SingleGameSourceDataUnit(game));
        }
        return new SimpleSourceDataSet(dataUnits);
    }

    private GameIndexerHelper createIndexer(List<Game> games) {
        return new GameIndexerHelper(games);
    }

    private ThreadingProfile createThreadingProfile() {
        ThreadingProfile profile = new ThreadingProfile();
        profile.setNumberOfThreads(NUM_GAMES);
        profile.setTimeoutSeconds(TIMEOUT_SECONDS);
        return profile;
    }

    private IndexMonitorHelper createMonitor() {
        return new IndexMonitorHelper();
    }

    // --- Test threaded indexing ---------------------------------------------

    @Test
    public void testIndexingSession() {
        session.run();

        IndexMonitorHelper monitor = (IndexMonitorHelper) session.getMonitor();
        assertTrue(monitor.getStarted());
        assertTrue(monitor.getFinished());
        assertFalse(monitor.getFailed());
        assertEquals(NUM_GAMES, monitor.getGamesCount());

        GameIndexerHelper indexer = (GameIndexerHelper) session.getIndexer();
        assertTrue(indexer.gamesIntersect());

        SimpleSourceDataSet dataSet = (SimpleSourceDataSet) session.getDataSet();
        for (SingleGameSourceDataUnit dataUnit : dataSet.getDataUnits()) {
            assertTrue(dataUnit.lifeCycleComplete());
        }
    }

    // --- Test null setters --------------------------------------------------

    @Test(expected = AssertionError.class)
    public void testNullDataSet() {
        session.setDataSet(null);
        session.run();
    }

    @Test(expected = AssertionError.class)
    public void testNullMonitor() {
        session.setMonitor(null);
        session.run();
    }

    @Test(expected = AssertionError.class)
    public void testNullIndexer() {
        session.setIndexer(null);
        session.run();
    }

    @Test(expected = AssertionError.class)
    public void testNullProfile() {
        session.setProfile(null);
        session.run();
    }

}
