package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        this.game = new Game();
    }

    // --- Test game data -----------------------------------------------------

    @Test
    public void testId() {
        assertNull(game.getId());
        final String id = "id";
        game.setId(id);
        assertEquals(id, game.getId());
    }

    @Test
    public void testWhite() {
        assertNull(game.getWhite());
        final String white = "White";
        game.setWhite(white);
        assertEquals(white, game.getWhite());
    }

    @Test
    public void testBlack() {
        assertNull(game.getBlack());
        final String black = "Black";
        game.setBlack(black);
        assertEquals(black, game.getBlack());
    }

    @Test
    public void testWhiteElo() {
        final int elo = 2500;
        game.setWhiteElo(elo);
        assertEquals(elo, game.getWhiteElo());
    }

    @Test
    public void testBlackElo() {
        final int elo = 2500;
        game.setBlackElo(elo);
        assertEquals(elo, game.getBlackElo());
    }

    @Test
    public void testResult() {
        final Result result = Result.BLACK_WINS;
        game.setResult(result);
        assertEquals(result, game.getResult());
    }

    @Test
    public void testMoveText() {
        MoveText moveText = new MoveText();
        moveText.addValue("foo");
        game.getMoveText().addValue("foo");
        assertEquals(moveText, game.getMoveText());
    }

    // --- Test structure -----------------------------------------------------

    @Test
    public void testEqualsHashCode() {
        Game first = new Game();
        assertTrue(first.equals(first));
        assertFalse(first.equals("a string"));

        Game second = new Game();
        assertTrue(first.equals(second));

        setNull(first, second);
        assertTrue(first.equals(second));
        second.setWhite("");
        assertFalse(first.equals(second));
        first.setWhite("");
        assertTrue(first.equals(second));
        second.setWhite("White");
        assertFalse(first.equals(second));

        setNull(first, second);
        second.setBlack("");
        assertFalse(first.equals(second));
        first.setBlack("");
        assertTrue(first.equals(second));
        second.setBlack("Black");
        assertFalse(first.equals(second));

        setNull(first, second);
        first.getMoveText().addValue("e4");
        assertFalse(first.equals(second));
        second.getMoveText().addValue("e4");
        assertTrue(first.equals(second));

        setNull(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        first.setWhite("White");
        second.setWhite("White");
        first.setBlack("Black");
        second.setBlack("Black");
        assertEquals(first.hashCode(), second.hashCode());
    }

    protected void setNull(Game first, Game second) {
        first.setWhite(null);
        second.setWhite(null);
        first.setBlack(null);
        second.setBlack(null);
    }

}
