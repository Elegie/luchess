package io.elegie.luchess.app.lucene_4x;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.app.lucene4x.explore.GameListQueryBuilder;
import io.elegie.luchess.app.lucene_4x.helpers.ServicesFactoryHelper;
import io.elegie.luchess.core.api.ServicesFactory;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.api.helpers.GameHelper;
import io.elegie.luchess.core.api.helpers.IndexHelper;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.Result;
import io.elegie.luchess.core.indexing.workflow.helpers.SimpleSourceDataSet;
import io.elegie.luchess.core.indexing.workflow.helpers.SingleGameSourceDataUnit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests related to our implementation of the {@link GameListQuery}, in its
 * specific behaviors.
 */
public class GameListQueryTest {

    private static final int MOVETEXT_LENGTH = 2;

    private ServicesFactory factory;
    private GameListQuery gameListQuery;

    @SuppressWarnings("javadoc")
    @Before
    public void setUp() throws BuildException, InterruptedException {
        factory = ServicesFactoryHelper.createInMemoryIndexFactory(false, MOVETEXT_LENGTH);
        indexTestGames();
        gameListQuery = factory.getExplorerService().createGameListQuery();
    }

    // --- Test Data ----------------------------------------------------------

    private static final String WHITE1 = "WhiteFoo";
    private static final String WHITE2 = "WhiteBar";
    private static final String WHITE3 = "WhiteLastName, WhiteFirstName";
    private static final String BLACK1 = "BlackFoo";
    private static final String BLACK2 = "BlackBar";
    private static final String BLACK3 = "BlackLastName, BlackFirstName";

    private Game game1;
    private Game game2;
    private Game game3;

    private void indexTestGames() throws BuildException, InterruptedException {
        game1 = GameHelper.createNewGame(WHITE1, BLACK1, 3000, 3000, Result.WHITE_WINS, "e4 e5");
        game2 = GameHelper.createNewGame(WHITE2, BLACK2, 2500, 2500, Result.BLACK_WINS, "e4 e5");
        game3 = GameHelper.createNewGame(WHITE3, BLACK3, 2400, 2400, Result.DRAW, "e4 e5");

        List<SingleGameSourceDataUnit> dataUnits = new ArrayList<>();
        dataUnits.add(new SingleGameSourceDataUnit(game1));
        dataUnits.add(new SingleGameSourceDataUnit(game2));
        dataUnits.add(new SingleGameSourceDataUnit(game3));
        SimpleSourceDataSet dataSet = new SimpleSourceDataSet(dataUnits);
        IndexHelper.indexDataSet(dataSet, factory);
    }

    // --- Names for either White or Black ------------------------------------

    /**
     * If the same name is filled in for White and Black, then we search for
     * both colors, combining the queries with an OR.
     * 
     * @throws QueryException
     */
    @Test
    public void testNamesForEitherColor() throws QueryException {
        gameListQuery.setWhite(WHITE1);
        gameListQuery.setBlack(WHITE1);
        validateOneGame(gameListQuery.execute());

        gameListQuery.setWhite(BLACK1);
        gameListQuery.setBlack(BLACK1);
        validateOneGame(gameListQuery.execute());
    }

    // --- Names with comma ---------------------------------------------------

    /**
     * Tests that we find games in the form "name, firstName", for White.
     * 
     * @throws QueryException
     */
    @Test
    public void testNamesWithCommasForWhite() throws QueryException {
        gameListQuery.setWhite("whitelastname");
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(game3, result.getGames().get(0));
    }

    /**
     * Tests that we find games in the form "name, firstName", for Black.
     * 
     * @throws QueryException
     */
    @Test
    public void testNamesWithCommasForBlack() throws QueryException {
        gameListQuery.setBlack("blacklastname");
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(1, result.getGames().size());
        assertEquals(game3, result.getGames().get(0));
    }

    // --- Multiple names -----------------------------------------------------

    /**
     * Our implementation allows several play names to be searched, if they are
     * separated by a comma. Check for White.
     * 
     * @throws QueryException
     */
    @Test
    public void testMultiplePlayerNamesForWhite() throws QueryException {
        gameListQuery.setWhite(WHITE1);
        validateOneGame(gameListQuery.execute());

        gameListQuery.setWhite(WHITE1 + "," + WHITE2);
        validateTwoGames(gameListQuery.execute());
    }

    /**
     * Our implementation allows several play names to be searched, if they are
     * separated by a comma. Check for Black.
     * 
     * @throws QueryException
     */
    @Test
    public void testMultiplePlayerNamesForBlack() throws QueryException {
        gameListQuery.setBlack(BLACK1);
        validateOneGame(gameListQuery.execute());

        gameListQuery.setBlack(BLACK1 + "," + BLACK2);
        validateTwoGames(gameListQuery.execute());
    }

    /**
     * Our implementation allows several play names to be searched, if they are
     * separated by a comma. Check for White and Black.
     * 
     * @throws QueryException
     */
    @Test
    public void testMultiplePlayerNamesForBoth() throws QueryException {
        gameListQuery.setWhite(WHITE1);
        gameListQuery.setBlack(BLACK1);
        validateOneGame(gameListQuery.execute());

        gameListQuery.setWhite(WHITE1 + "," + WHITE2);
        gameListQuery.setBlack(BLACK1 + "," + BLACK2);
        validateTwoGames(gameListQuery.execute());
    }

    // --- Exact match using double quotes ------------------------------------

    /**
     * Exact matches may be specified by surrounding the player's name by double
     * quotes. Test for White.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchDoubleQuotesWhite() throws QueryException {
        gameListQuery.setWhite(createSimilarName(WHITE1));
        validateOneGame(gameListQuery.execute());

        gameListQuery.setWhite(doubleQuote(createSimilarName(WHITE1)));
        validateNoGame(gameListQuery.execute());

        gameListQuery.setWhite(createSimilarName(WHITE1) + "," + doubleQuote(createSimilarName(WHITE1)));
        validateOneGame(gameListQuery.execute());
    }

    /**
     * Exact matches may be specified by surrounding the player's name by double
     * quotes. Test for Black.
     * 
     * @throws QueryException
     */
    @Test
    public void testExactMatchDoubleQuotesBlack() throws QueryException {
        gameListQuery.setBlack(createSimilarName(BLACK1));
        validateOneGame(gameListQuery.execute());

        gameListQuery.setBlack(doubleQuote(createSimilarName(BLACK1)));
        validateNoGame(gameListQuery.execute());

        gameListQuery.setBlack(createSimilarName(BLACK1) + "," + doubleQuote(createSimilarName(BLACK1)));
        validateOneGame(gameListQuery.execute());
    }

    private String createSimilarName(String name) {
        return "A" + name + "Z";
    }

    private String doubleQuote(String name) {
        return "\"" + name + "\"";
    }

    // --- Elo ---------------------------------------------------------------

    /**
     * @throws QueryException
     *             When the specified elo is too high.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnreasonableElo() throws QueryException {
        gameListQuery.setMinElo(GameListQueryBuilder.MAX_ELO + 1);
        gameListQuery.execute();
    }

    /**
     * Games with high elo ratings are returned first.
     * 
     * @throws QueryException
     * @throws InterruptedException
     * @throws BuildException
     */
    @Test
    public void testEloBoost() throws QueryException, BuildException, InterruptedException {
        GameListQueryResult result = gameListQuery.execute();
        assertEquals(3, result.getGames().size());
        assertTrue(game1.equals(result.getGames().get(0)));
        assertTrue(game2.equals(result.getGames().get(1)));
        assertTrue(game3.equals(result.getGames().get(2)));

        Game superGame = GameHelper.createNewGame("", "", 3500, 3500, Result.BLACK_WINS, "e4 e5");
        List<SingleGameSourceDataUnit> dataUnits = new ArrayList<>();
        dataUnits.add(new SingleGameSourceDataUnit(superGame));
        SimpleSourceDataSet dataSet = new SimpleSourceDataSet(dataUnits);
        IndexHelper.indexDataSet(dataSet, factory);

        result = gameListQuery.execute();
        assertEquals(4, result.getGames().size());
        assertTrue(superGame.equals(result.getGames().get(0)));
        assertTrue(game1.equals(result.getGames().get(1)));
        assertTrue(game2.equals(result.getGames().get(2)));
        assertTrue(game3.equals(result.getGames().get(3)));
    }

    // --- Helpers ------------------------------------------------------------

    private void validateNoGame(GameListQueryResult result) {
        assertEquals(0, result.getGames().size());
    }

    private void validateOneGame(GameListQueryResult result) {
        assertEquals(1, result.getGames().size());
        assertEquals(game1, result.getGames().get(0));
    }

    private void validateTwoGames(GameListQueryResult result) {
        assertEquals(2, result.getGames().size());
        assertTrue(game1.equals(result.getGames().get(0)) || game1.equals(result.getGames().get(0)));
        assertTrue(game2.equals(result.getGames().get(1)) || game2.equals(result.getGames().get(1)));
    }
}
