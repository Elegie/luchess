package io.elegie.luchess.core.domain.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

import org.junit.Before;
import org.junit.Test;

/**
 * Checks that the disambiguation is properly cleaned in regards of the pieces
 * already positioned.
 */
@SuppressWarnings("javadoc")
public class DisambiguationCleaningTest {

    private static final Color COLOR = Color.WHITE;
    private static final Piece QUEEN = new Piece(Figurine.QUEEN, Color.WHITE);

    /**
     * e4
     */
    private static final Vertex TARGET = new Vertex('e', 4);

    /**
     * d3
     */
    private static final Vertex SOURCE = new Vertex('d', 3);

    /**
     * d4
     */
    private static final Vertex DISAMBIGUATION_BY_ROW_1 = new Vertex('d', 4);

    /**
     * d5
     */
    private static final Vertex DISAMBIGUATION_BY_ROW_2 = new Vertex('d', 5);

    /**
     * e3
     */
    private static final Vertex DISAMBIGUATION_BY_COL_1 = new Vertex('e', 3);

    /**
     * f3
     */
    private static final Vertex DISAMBIGUATION_BY_COL_2 = new Vertex('f', 3);

    /**
     * f5
     */
    private static final Vertex DISAMBIGUATION_BY_ROW_COL = new Vertex('f', 5);

    /**
     * z9
     */
    private static final Vertex INVALID_VERTEX = new Vertex('z', 9);

    private QueenMove move;
    private Position position;

    @Before
    public void setUp() {
        move = new QueenMove();
        move.setTarget(TARGET);
        position = new Position();
    }

    @Test
    public void testNoTargetNoDisambiguation() throws IllegalMoveException {
        move.setDisambiguation(null);
        move.setTarget(null);
        move.cleanDisambiguation(COLOR, position);
        assertNull(move.getDisambiguation());
        assertNull(move.getTarget());

        move.setTarget(INVALID_VERTEX);
        move.cleanDisambiguation(COLOR, position);
        assertNull(move.getDisambiguation());
        assertEquals(INVALID_VERTEX, move.getTarget());
    }

    @Test
    public void testNoNeedToDisambiguate() throws IllegalMoveException {
        move.setDisambiguation(INVALID_VERTEX);
        move.cleanDisambiguation(COLOR, position);
        assertNull(move.getDisambiguation());

        move.setDisambiguation(INVALID_VERTEX);
        position.setPiece(SOURCE, QUEEN);
        move.cleanDisambiguation(COLOR, position);
        assertNull(move.getDisambiguation());
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidColRow() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_COL_1, QUEEN);
        move.setDisambiguation(INVALID_VERTEX);
        move.cleanDisambiguation(COLOR, position);
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidColTooManyRows() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_COL_1, QUEEN);
        move.setDisambiguation(new Vertex(INVALID_VERTEX.getCol(), SOURCE.getRow()));
        move.cleanDisambiguation(COLOR, position);
    }

    @Test
    public void testInvalidColOneRowWithColDisambiguation() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_COL, QUEEN);
        move.setDisambiguation(new Vertex(INVALID_VERTEX.getCol(), SOURCE.getRow()));
        move.cleanDisambiguation(COLOR, position);
        assertFalse(move.getDisambiguation().isValidRow());
        assertEquals(SOURCE.getCol(), move.getDisambiguation().getCol());
    }

    @Test
    public void testInvalidColOneRowWithRowDisambiguation() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_1, QUEEN);
        move.setDisambiguation(new Vertex(INVALID_VERTEX.getCol(), SOURCE.getRow()));
        move.cleanDisambiguation(COLOR, position);
        assertEquals(SOURCE.getRow(), move.getDisambiguation().getRow());
        assertFalse(move.getDisambiguation().isValidCol());
    }

    @Test(expected = IllegalMoveException.class)
    public void testInvalidRowTooManyCols() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_1, QUEEN);
        move.setDisambiguation(new Vertex(SOURCE.getCol(), INVALID_VERTEX.getRow()));
        move.cleanDisambiguation(COLOR, position);
    }

    @Test
    public void testInvalidRowOneCol() throws IllegalMoveException {
        position.setPiece(SOURCE, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_COL_2, QUEEN);
        move.setDisambiguation(new Vertex(SOURCE.getCol(), INVALID_VERTEX.getRow()));
        move.cleanDisambiguation(COLOR, position);
    }

    @Test
    public void testColNotDiscriminatedRowDiscriminated() throws IllegalMoveException {
        position.setPiece(DISAMBIGUATION_BY_COL_1, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_COL, QUEEN);
        move.setDisambiguation(SOURCE);
        move.cleanDisambiguation(COLOR, position);
        assertEquals(SOURCE.getRow(), move.getDisambiguation().getRow());
        assertFalse(move.getDisambiguation().isValidCol());
    }

    @Test
    public void testRowNotDiscriminatedColDiscriminated() throws IllegalMoveException {
        position.setPiece(DISAMBIGUATION_BY_ROW_1, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_COL, QUEEN);
        move.setDisambiguation(SOURCE);
        move.cleanDisambiguation(COLOR, position);
        assertEquals(SOURCE.getCol(), move.getDisambiguation().getCol());
        assertFalse(move.getDisambiguation().isValidRow());
    }

    @Test(expected = IllegalMoveException.class)
    public void testZeroIntersect() throws IllegalMoveException {
        position.setPiece(DISAMBIGUATION_BY_COL_1, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_COL_2, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_1, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_2, QUEEN);
        move.setDisambiguation(SOURCE);
        move.cleanDisambiguation(COLOR, position);
    }

    @Test
    public void testOneIntersect() throws IllegalMoveException {
        position.setPiece(DISAMBIGUATION_BY_COL_1, QUEEN);
        position.setPiece(DISAMBIGUATION_BY_ROW_1, QUEEN);
        position.setPiece(SOURCE, QUEEN);
        move.setDisambiguation(SOURCE);
        move.cleanDisambiguation(COLOR, position);
    }

}
