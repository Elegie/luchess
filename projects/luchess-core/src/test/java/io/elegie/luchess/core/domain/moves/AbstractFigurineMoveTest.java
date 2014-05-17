package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.helpers.PositionHelper;

import java.util.List;

import org.junit.Test;

/**
 * Tests the main move algorithm for the given move type.
 */
public abstract class AbstractFigurineMoveTest {

    protected static final Color PLAYER_COLOR = Color.WHITE;
    protected static final Vertex SOURCE_VERTEX = new Vertex('e', 4);

    /**
     * @return The class from which the move should be created (e.g. a
     *         BishopMove.class).
     */
    protected abstract Class<? extends AbstractFigurineMove> getFigurineMove();

    /**
     * @return Some vertices on which the piece located on the source target may
     *         be put.
     */
    protected abstract List<Vertex> getValidTargets();

    // --- Nominal Tests ------------------------------------------------------

    /**
     * Check that all proposed valid targets can be reached from the source, for
     * the given piece.
     * 
     * @throws IllegalMoveException
     */
    @Test
    public void testMovePerformed() throws IllegalMoveException {
        testAllTargets(false, Figurine.PAWN);
    }

    /**
     * We put an enemy piece onto the target vertex, and check that is has been
     * properly captured.
     * 
     * @throws IllegalMoveException
     */
    @Test
    public void testCapturePerformed() throws IllegalMoveException {
        testAllTargets(true, Figurine.PAWN);
    }

    /**
     * A piece cannot capture a king.
     * 
     * @throws IllegalMoveException
     */
    @Test(expected = IllegalMoveException.class)
    public void testCaptureKingFailed() throws IllegalMoveException {
        testAllTargets(true, Figurine.KING);
    }

    /**
     * @throws IllegalMoveException
     *             Because the position is empty, so the move cannot be applied.
     */
    @Test(expected = IllegalMoveException.class)
    public void testCannotApply() throws IllegalMoveException {
        Position emptyPosition = new Position();
        createMove().apply(Color.WHITE, emptyPosition);
    }

    private void testAllTargets(boolean includeEnemies, Figurine capturedFigurine) throws IllegalMoveException {
        AbstractFigurineMove move = createMove();
        Figurine figurine = move.getFigurine();
        for (Vertex target : getValidTargets()) {
            Position position = new Position();
            position.setPiece(SOURCE_VERTEX, new Piece(figurine, PLAYER_COLOR));
            if (includeEnemies) {
                position.setPiece(target, new Piece(capturedFigurine, PLAYER_COLOR.invert()));
            }
            move.setTarget(target);
            move.apply(PLAYER_COLOR, position);
            PositionHelper.validateEndPosition(position, target, new Piece(figurine, PLAYER_COLOR));
        }
    }

    // --- Value Tests --------------------------------------------------------

    /**
     * Tests that the value is properly constructed, taking figurine,
     * disambiguation and target into account.
     */
    @Test
    public void testValue() {
        AbstractFigurineMove move = createMove();
        Figurine figurine = move.getFigurine();
        assertEquals(figurine.toString(), move.getUncheckedValue());

        move.setDisambiguation(new Vertex(' ', 0));
        assertEquals(figurine.toString(), move.getUncheckedValue());

        move.setDisambiguation(new Vertex('e', 0));
        assertEquals(figurine.toString() + 'e', move.getUncheckedValue());

        move.setDisambiguation(new Vertex(' ', 4));
        assertEquals(figurine.toString() + '4', move.getUncheckedValue());

        move.setDisambiguation(new Vertex(' ', 0));
        move.setCaptures(true);
        assertEquals(figurine.toString() + 'x', move.getUncheckedValue());
        move.setCaptures(false);

        move.setTarget(new Vertex('e', 0));
        assertEquals(figurine.toString() + 'e', move.getUncheckedValue());

        move.setTarget(new Vertex(' ', 4));
        assertEquals(figurine.toString() + '4', move.getUncheckedValue());

        move.setDisambiguation(new Vertex('e', 4));
        move.setTarget(new Vertex('f', 5));
        move.setCaptures(true);
        assertEquals(figurine.toString() + "e4xf5", move.getUncheckedValue());
    }

    // --- Helpers ------------------------------------------------------------

    private AbstractFigurineMove createMove() {
        try {
            AbstractFigurineMove move = getFigurineMove().newInstance();
            return move;
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "%s when creating a move.";
            message = String.format(message, e.getClass().getSimpleName());
            fail(message);
        }
        return null;
    }

}
