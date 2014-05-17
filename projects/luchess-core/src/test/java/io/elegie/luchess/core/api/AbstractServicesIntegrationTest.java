package io.elegie.luchess.core.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.GameFindQueryResult;
import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.api.helpers.GameHelper;
import io.elegie.luchess.core.api.helpers.IndexHelper;
import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.core.domain.entities.Result;
import io.elegie.luchess.core.indexing.workflow.helpers.SimpleSourceDataSet;
import io.elegie.luchess.core.indexing.workflow.helpers.SingleGameSourceDataUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 * This is a suite of tests, which should validate all services provided by any
 * conforming implementation. To use this suite, implementations should simply
 * extend this class, providing their own initialized factory, making sure to
 * call super.setUp() if they override the setUp() method.
 */
public abstract class AbstractServicesIntegrationTest implements ServicesFactoryProvider {

    /**
     * The number of moves per game, so that the implementation may adjust its
     * analysis depth or the like.
     */
    protected static final int MOVETEXT_LENGTH = 15;

    /**
     * @return true if the implementation supports approximate searches on
     *         players' names.
     */
    protected abstract boolean testSimilarPlayerNameMatch();

    private ServicesFactory factory;
    private GameListQuery gameListQuery;

    /**
     * By default, we create an index in create mode, and populate it with some
     * test data. Tests requiring a different setup should adjust the setup
     * themselves.
     */
    @Before
    public void setUp() {
        factory = createServicesFactory(true);

        try {
            populateIndexWithTestData();
        } catch (BuildException | InterruptedException e) {
            fail("Cannot populate index: " + e);
        }

        gameListQuery = factory.getExplorerService().createGameListQuery();
        gameListQuery.setPageCount(NUMBER_OF_GAMES);
    }

    // --- Test Data ----------------------------------------------------------

    private static final int TOTAL_WHITE_WINS = 3;
    private static final int TOTAL_BLACK_WINS = 1;
    private static final int TOTAL_DRAWS = 1;
    private static final int TOTAL_UNFINISHED = 1;
    private static final int NUMBER_OF_GAMES = TOTAL_WHITE_WINS + TOTAL_BLACK_WINS + TOTAL_DRAWS + TOTAL_UNFINISHED;

    private Game gameExactMatch;
    private Game gameSimilarMatch;
    private Game gameHighElo;
    private Game singleE4Game;
    private Game richGame;
    private Game unfinishedGame;

    private void populateIndexWithTestData() throws BuildException, InterruptedException {
        gameExactMatch = GameHelper.createNewGame("WhiteExact", "BlackExact", 2500, 2600, Result.WHITE_WINS,
                "d4 Nf6 c4 g6 g3 Bg7 Bg2 O-O Nc3 a6 h3 h6 h4 h5 g4");
        gameSimilarMatch = GameHelper.createNewGame("WhiteSim", "BlackSim", 2500, 2600, Result.BLACK_WINS,
                "d4 Nf6 c4 g6 g3 Bg7 Bg2 O-O Nc3 d6 h3 h6 h4 h5 g4");
        gameHighElo = GameHelper.createNewGame("WhiteStrong", "BlackStrong", 3000, 2900, Result.DRAW,
                "d4 Nc6 c4 g6 g3 Bg7 Bg2 O-O Nc3 d6 h3 h6 h4 h5 g4");
        singleE4Game = GameHelper.createNewGame("WhiteKing", "BlackKing", 2500, 2600, Result.WHITE_WINS,
                "e4 Nc6 c4 g6 g3 Bg7 Bg2 O-O Nc3 d6 h3 h6 h4 h5 g4");
        richGame = GameHelper.createNewGame("WhiteRich", "BlackRich", 2500, 2600, Result.WHITE_WINS,
                "d4 e5 dxe5 a6 e6 a5 exf7+ Ke7 fxg8=Q Kf6 Qxf8+ Kg5 Qd5 Kh4 Qf4#");
        unfinishedGame = GameHelper.createNewGame("WhiteUnfinished", "BlackUnfinished", 2500, 2600, Result.UNFINISHED,
                "Nf3");

        List<SingleGameSourceDataUnit> dataUnits = new ArrayList<>();
        dataUnits.add(new SingleGameSourceDataUnit(gameExactMatch));
        dataUnits.add(new SingleGameSourceDataUnit(gameSimilarMatch));
        dataUnits.add(new SingleGameSourceDataUnit(gameHighElo));
        dataUnits.add(new SingleGameSourceDataUnit(singleE4Game));
        dataUnits.add(new SingleGameSourceDataUnit(richGame));
        dataUnits.add(new SingleGameSourceDataUnit(unfinishedGame));
        SimpleSourceDataSet dataSet = new SimpleSourceDataSet(dataUnits);
        IndexHelper.indexDataSet(dataSet, factory);
    }

    // ------------------------------------------------------------------------
    // IndexInfoService
    // ------------------------------------------------------------------------

    /**
     * When initializing the factory in create mode, there should be no games in
     * the index.
     * 
     * @throws QueryException
     */
    @Test(expected = QueryException.class)
    public void testNoGames() throws QueryException {
        factory = createServicesFactory(true);
        assertEquals(0, factory.getIndexInfoService().getGamesCount());
    }

    /**
     * Test that all test games are in the index, using the IndexInfo service.
     * 
     * @throws QueryException
     */
    @Test
    public void testAllGames() throws QueryException {
        assertEquals(NUMBER_OF_GAMES, factory.getIndexInfoService().getGamesCount());
    }

    // ------------------------------------------------------------------------
    // GameFindQuery
    // ------------------------------------------------------------------------

    /**
     * Insert a game in the index, grab back its id, and check that the game can
     * be retrieved by its id.
     * 
     * @throws BuildException
     * @throws InterruptedException
     * @throws QueryException
     */
    @Test
    public void testGameFound() throws BuildException, InterruptedException, QueryException {
        Game game = GameHelper.createNewGame(0, "e4");
        SingleGameSourceDataUnit dataUnit = new SingleGameSourceDataUnit(game);
        SimpleSourceDataSet dataSet = new SimpleSourceDataSet(dataUnit);
        IndexHelper.indexDataSet(dataSet, factory);
        assertNotNull("ID was generated when indexing", game.getId());

        GameFindQuery query = factory.getExplorerService().createGameFindQuery();
        query.setId(game.getId());
        GameFindQueryResult result = query.execute();
        assertEquals(game, result.getGame());
    }

    /**
     * No game returned when the specified ID does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testWrongId() throws QueryException {
        GameFindQuery query = factory.getExplorerService().createGameFindQuery();
        query.setId("unknown id");
        GameFindQueryResult result = query.execute();
        assertNull(result.getGame());
    }

    // ------------------------------------------------------------------------
    // GameListQuery
    // ------------------------------------------------------------------------

    // --- Testing Game Update ------------------------------------------------

    /**
     * Check that a game is properly updated when in update mode.
     * 
     * @throws BuildException
     * @throws InterruptedException
     * @throws QueryException
     */
    @Test
    public void testGameUpdate() throws BuildException, InterruptedException, QueryException {
        // Create a new factory in update mode
        factory = createServicesFactory(false);

        // Insert our game, starting by "d4"
        Game game = GameHelper.createNewGame(0, "d4");
        SingleGameSourceDataUnit dataUnit = new SingleGameSourceDataUnit(game);
        SimpleSourceDataSet dataSet = new SimpleSourceDataSet(dataUnit);
        IndexHelper.indexDataSet(dataSet, factory);
        assertEquals(1, factory.getIndexInfoService().getGamesCount());

        // Grab the ID, for further checks
        String gameId = game.getId();
        assertNotNull("ID was generated when indexing", gameId);

        // Retrieve our game, looking for games starting by "d4"
        GameListQuery query = factory.getExplorerService().createGameListQuery();
        query.setMoves(Arrays.asList("d4"));
        GameListQueryResult result = query.execute();
        assertEquals(game, result.getGames().get(0));

        // Define new values for the update (now starts with e4)
        String newWhiteValue = "newWhiteValue";
        String newBlackValue = "newBlackValue";
        int newWhiteEloValue = 3000;
        int newBlackEloValue = 3100;
        Game newGame = new Game();
        newGame.setId(game.getId());
        newGame.setBlack(newBlackValue);
        newGame.setBlackElo(newBlackEloValue);
        newGame.setWhite(newWhiteValue);
        newGame.setWhiteElo(newWhiteEloValue);
        newGame.getMoveText().addValue("e4");

        // Update the game
        dataUnit = new SingleGameSourceDataUnit(newGame);
        dataSet = new SimpleSourceDataSet(dataUnit);
        IndexHelper.indexDataSet(dataSet, factory);
        assertEquals(1, factory.getIndexInfoService().getGamesCount());

        // Check that the game has been updated
        query = factory.getExplorerService().createGameListQuery();
        query.setMoves(Arrays.asList("e4"));
        result = query.execute();
        assertEquals(newGame, result.getGames().get(0));
        assertEquals(gameId, newGame.getId());
        assertEquals(newWhiteValue, newGame.getWhite());
        assertEquals(newBlackValue, newGame.getBlack());
        assertEquals(newWhiteEloValue, newGame.getWhiteElo());
        assertEquals(newBlackEloValue, newGame.getBlackElo());
        assertEquals("e4", newGame.getMoveText().getValue());
    }

    // --- Testing No Criteria ------------------------------------------------

    /**
     * When specifying no search criteria, then the query should return all
     * games, using paging defaults.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoCriteria() throws QueryException {
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(NUMBER_OF_GAMES,
                result.getTotalBlackWins() + result.getTotalDraws() + result.getTotalWhiteWins() + result.getTotalUnfinished());
        assertEquals(TOTAL_WHITE_WINS, result.getTotalWhiteWins());
        assertEquals(TOTAL_BLACK_WINS, result.getTotalBlackWins());
        assertEquals(TOTAL_DRAWS, result.getTotalDraws());
        assertEquals(TOTAL_UNFINISHED, result.getTotalUnfinished());
    }

    // --- Testing players' names ---------------------------------------------

    /**
     * Find games where White exactly matches the searched name. Names should
     * not be case-sensitive.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchForWhite() throws QueryException {
        GameListQueryResult result = testPlayerMatch(gameExactMatch.getWhite(), "", 1);
        assertEquals(gameExactMatch, result.getGames().get(0));

        result = testPlayerMatch(gameExactMatch.getWhite().toLowerCase(), "", 1);
        assertEquals(gameExactMatch, result.getGames().get(0));

        result = testPlayerMatch(gameExactMatch.getWhite().toUpperCase(), "", 1);
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    /**
     * Find games where Black exactly matches the searched name. Names should
     * not be case-sensitive.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchForBlack() throws QueryException {
        GameListQueryResult result = testPlayerMatch("", gameExactMatch.getBlack(), 1);
        assertEquals(gameExactMatch, result.getGames().get(0));

        result = testPlayerMatch("", gameExactMatch.getBlack().toLowerCase(), 1);
        assertEquals(gameExactMatch, result.getGames().get(0));

        result = testPlayerMatch("", gameExactMatch.getBlack().toUpperCase(), 1);
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    /**
     * Check that a game is found when White's name has been approximately
     * matched.
     * 
     * @throws QueryException
     */
    @Test
    public void testSimilarMatchForWhite() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        GameListQueryResult result = testPlayerMatch(createSimilarName(gameSimilarMatch.getWhite()), "", 1);
        assertEquals(gameSimilarMatch, result.getGames().get(0));
    }

    /**
     * Check that a game is found when Black's name has been approximately
     * matched.
     * 
     * @throws QueryException
     */
    @Test
    public void testSimilarMatchForBlack() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        GameListQueryResult result = testPlayerMatch("", createSimilarName(gameSimilarMatch.getBlack()), 1);
        assertEquals(gameSimilarMatch, result.getGames().get(0));
    }

    /**
     * No games returned when White does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoMatchForWhite() throws QueryException {
        testPlayerMatch(createRandomName(), "", 0);
    }

    /**
     * No games returned when Black does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoMatchForBlack() throws QueryException {
        testPlayerMatch("", createRandomName(), 0);
    }

    /**
     * Game returned when both players' names match.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchForBoth() throws QueryException {
        GameListQueryResult result = testPlayerMatch(gameExactMatch.getWhite(), gameExactMatch.getBlack(), 1);
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    /**
     * Game returned when both players' names approximately match.
     * 
     * @throws QueryException
     */
    @Test
    public void testSimilarMatchForBoth() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        GameListQueryResult result = testPlayerMatch(
                createSimilarName(gameSimilarMatch.getWhite()),
                createSimilarName(gameSimilarMatch.getBlack()),
                1);
        assertEquals(gameSimilarMatch, result.getGames().get(0));
    }

    /**
     * No game returned when both players' names do not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoMatchForBoth() throws QueryException {
        testPlayerMatch(createRandomName(), createRandomName(), 0);
    }

    /**
     * No game returned when two names are specified, but Black does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchForWhiteNoMatchForBlack() throws QueryException {
        testPlayerMatch(gameExactMatch.getWhite(), createRandomName(), 0);
    }

    /**
     * Game found when both names are specified, White exactly and Black
     * approximately.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchForWhiteSimilarMatchForBlack() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        GameListQueryResult result = testPlayerMatch(
                gameExactMatch.getWhite(),
                createSimilarName(gameExactMatch.getBlack()),
                1);
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    /**
     * No game returned when two names are specified, White approximately, but
     * Black does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testSimilarMatchForWhiteNoMatchForBlack() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        testPlayerMatch(createSimilarName(gameSimilarMatch.getWhite()), createRandomName(), 0);
    }

    /**
     * Game found when both names are specified, White approximately and Black
     * exactly.
     * 
     * @throws QueryException
     */
    @Test
    public void testSimilarMatchForWhiteExactMatchForBlack() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        GameListQueryResult result = testPlayerMatch(
                createSimilarName(gameExactMatch.getWhite()),
                gameExactMatch.getBlack(),
                1);
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    /**
     * No game returned when two names are specified, Black exactly, but White
     * does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoMatchForWhiteExactMatchForBlack() throws QueryException {
        testPlayerMatch(createRandomName(), gameExactMatch.getBlack(), 0);
    }

    /**
     * No game returned when two names are specified, Black approximately, but
     * White does not exist.
     * 
     * @throws QueryException
     */
    @Test
    public void testNoMatchForWhiteSimilarMatchForBlack() throws QueryException {
        if (!testSimilarPlayerNameMatch()) {
            return;
        }

        testPlayerMatch(createRandomName(), createSimilarName(gameSimilarMatch.getBlack()), 0);
    }

    private String createRandomName() {
        return UUID.randomUUID().toString();
    }

    private String createSimilarName(String name) {
        return "A" + name + "Z";
    }

    private GameListQueryResult testPlayerMatch(String white, String black, int numGames) throws QueryException {
        gameListQuery.setWhite(white);
        gameListQuery.setBlack(black);
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(numGames, result.getGames().size());
        return result;
    }

    // --- Testing players' elos ----------------------------------------------

    /**
     * Only games which at least one player elo being above the specified
     * minimum elo are returned.
     * 
     * @throws QueryException
     */
    @Test
    public void testMinElo() throws QueryException {
        gameListQuery.setMinElo(gameHighElo.getWhiteElo());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameHighElo, result.getGames().get(0));
    }

    /**
     * No game found if the specified elo is too high.
     * 
     * @throws QueryException
     */
    @Test
    public void testMinEloTooHigh() throws QueryException {
        gameListQuery.setMinElo(gameHighElo.getWhiteElo() + 1);
        assertEquals(0, gameListQuery.execute().getGames().size());
    }

    /**
     * Check that the combination of elo/White works fine.
     * 
     * @throws QueryException
     */
    @Test
    public void testMinEloWithExactMatchForWhite() throws QueryException {
        gameListQuery.setWhite(gameExactMatch.getWhite());
        testMinEloWithPlayerName();
    }

    /**
     * Check that the combination of elo/Black works fine.
     * 
     * @throws QueryException
     */
    @Test
    public void testMinEloWithExactMatchForBlack() throws QueryException {
        gameListQuery.setBlack(gameExactMatch.getBlack());
        testMinEloWithPlayerName();
    }

    private void testMinEloWithPlayerName() throws QueryException {
        int minElo = Math.min(gameExactMatch.getWhiteElo(), gameExactMatch.getBlackElo());
        gameListQuery.setMinElo(minElo);
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameExactMatch, result.getGames().get(0));

        int maxElo = Math.max(gameExactMatch.getWhiteElo(), gameExactMatch.getBlackElo());
        gameListQuery.setMinElo(maxElo + 1);
        assertEquals(0, gameListQuery.execute().getGames().size());

        int middleElo = (minElo + maxElo) / 2;
        gameListQuery.setMinElo(middleElo);
        result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameExactMatch, result.getGames().get(0));
    }

    // --- Testing moves ------------------------------------------------------

    /**
     * Search games by first moves, and check that continuations are properly
     * calculated.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMove() throws QueryException {
        gameListQuery.setMoves(Arrays.asList("d4"));
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(4, result.getGames().size());
        assertEquals(2, result.getTotalWhiteWins());
        assertEquals(1, result.getTotalBlackWins());
        assertEquals(1, result.getTotalDraws());

        List<Continuation> continuations = result.getContinuations();
        assertEquals(3, continuations.size());
        for (Continuation continuation : continuations) {
            String move = continuation.getMove().getValue();
            if (move.equals("Nf6")) {
                assertEquals(1, continuation.getTotalWhiteWins());
                assertEquals(1, continuation.getTotalBlackWins());
                assertEquals(0, continuation.getTotalDraws());
            } else if (move.equals("Nc6")) {
                assertEquals(0, continuation.getTotalWhiteWins());
                assertEquals(0, continuation.getTotalBlackWins());
                assertEquals(1, continuation.getTotalDraws());
            } else if (move.equals("e5")) {
                assertEquals(1, continuation.getTotalWhiteWins());
                assertEquals(0, continuation.getTotalBlackWins());
                assertEquals(0, continuation.getTotalDraws());
            } else {
                fail("unexpected continuation: " + continuation);
            }
        }
    }

    /**
     * Find a game with some moves having special characters (=, +, #).
     * 
     * @throws QueryException
     */
    @Test
    public void testFindWithSpecialChars() throws QueryException {
        List<String> moves = Arrays.asList(richGame.getMoveText().getValue().split(Character.toString(MoveText.SEPARATOR)));
        gameListQuery.setMoves(moves.subList(0, 7));
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(richGame, result.getGames().get(0));
        assertEquals(1, result.getContinuations().size());
    }

    /**
     * Find a game by inputting all of its moves. This should branch us beyond
     * the analysis depth. No continuations should be returned (no move beyond).
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByAllMoves() throws QueryException {
        List<String> moves = Arrays.asList(richGame.getMoveText().getValue().split(Character.toString(MoveText.SEPARATOR)));
        gameListQuery.setMoves(moves);
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(richGame, result.getGames().get(0));
        assertEquals(0, result.getContinuations().size());
    }

    /**
     * Test the combination of move/elo.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMoveWithMineElo() throws QueryException {
        gameListQuery.setMoves(getFirstMove(gameHighElo));
        gameListQuery.setMinElo(gameHighElo.getWhiteElo());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameHighElo, result.getGames().get(0));

        gameListQuery.setMoves(getFirstMove(singleE4Game));
        assertEquals(0, gameListQuery.execute().getGames().size());
    }

    /**
     * Test the combination of move/White.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMoveWithExactMatchForWhite() throws QueryException {
        gameListQuery.setMoves(getFirstMove(gameExactMatch));
        gameListQuery.setWhite(gameExactMatch.getWhite());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameExactMatch, result.getGames().get(0));

        gameListQuery.setWhite(singleE4Game.getWhite());
        assertEquals(0, gameListQuery.execute().getGames().size());
    }

    /**
     * Test the combination of move/Black.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMoveWithExactMatchForBlack() throws QueryException {
        gameListQuery.setMoves(getFirstMove(gameExactMatch));
        gameListQuery.setBlack(gameExactMatch.getBlack());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameExactMatch, result.getGames().get(0));

        gameListQuery.setBlack(singleE4Game.getBlack());
        assertEquals(0, gameListQuery.execute().getGames().size());
    }

    /**
     * Test the combination of move/White/elo.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMoveWithExactMatchForWhiteAndMinElo() throws QueryException {
        gameListQuery.setMoves(getFirstMove(gameHighElo));
        gameListQuery.setWhite(gameHighElo.getWhite());
        gameListQuery.setMinElo(gameHighElo.getWhiteElo());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameHighElo, result.getGames().get(0));
    }

    /**
     * Test the combination of move/Black/elo.
     * 
     * @throws QueryException
     */
    @Test
    public void testFindByFirstMoveWithExactMatchForBlackAndMinElo() throws QueryException {
        gameListQuery.setMoves(getFirstMove(gameHighElo));
        gameListQuery.setBlack(gameHighElo.getBlack());
        gameListQuery.setMinElo(gameHighElo.getBlackElo());
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(gameHighElo, result.getGames().get(0));
    }

    private List<String> getFirstMove(Game game) {
        List<String> moves = Arrays.asList(game.getMoveText().getValue().split(Character.toString(MoveText.SEPARATOR)));
        return Arrays.asList(moves.get(0));
    }

    // --- Test paging --------------------------------------------------------

    /**
     * Test that the pages are properly built, contain the right amount of
     * games, and not the same games.
     * 
     * @throws QueryException
     */
    @Test
    public void testPaging() throws QueryException {
        final int FIRST_PAGE = 1;
        final int NUM_PAGES = 2;
        final int NUM_GAMES_PER_PAGE = NUMBER_OF_GAMES / NUM_PAGES;

        gameListQuery.setPageCount(NUM_GAMES_PER_PAGE);
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(NUMBER_OF_GAMES,
                result.getTotalBlackWins() + result.getTotalDraws() + result.getTotalWhiteWins() + result.getTotalUnfinished());
        assertEquals(NUM_GAMES_PER_PAGE, result.getGames().size());
        List<Game> first = result.getGames();

        gameListQuery.setPageStart(FIRST_PAGE + 1);
        result = gameListQuery.execute();
        assertEquals(NUMBER_OF_GAMES,
                result.getTotalBlackWins() + result.getTotalDraws() + result.getTotalWhiteWins() + result.getTotalUnfinished());
        assertEquals(NUM_GAMES_PER_PAGE, result.getGames().size());
        List<Game> second = result.getGames();

        for (Game game : first) {
            assertFalse(second.contains(game));
        }
        for (Game game : second) {
            assertFalse(first.contains(game));
        }
    }

    /**
     * Test that no game is returned if the requested page is too far away.
     * 
     * @throws QueryException
     */
    @Test
    public void testBeyondLastPage() throws QueryException {
        final int FIRST_PAGE = 1;

        gameListQuery.setPageCount(NUMBER_OF_GAMES + 1);
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(NUMBER_OF_GAMES, result.getGames().size());

        gameListQuery.setPageStart(FIRST_PAGE + 1);
        result = gameListQuery.execute();
        assertEquals(0, result.getGames().size());
    }

}
