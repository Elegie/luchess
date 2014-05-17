package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.exceptions.InvalidVertexException;

import org.junit.Test;

/**
 * Test all methods of the Vertex. A Vertex centralizes all methods related to
 * positioning onto a board.
 */
@SuppressWarnings("javadoc")
public class VertexTest {

    @Test
    public void testStructure() {
        final char col = 'e';
        final int row = 4;
        Vertex vertex = new Vertex(col, row);
        assertEquals(col, vertex.getCol());
        assertEquals(row, vertex.getRow());
    }

    @Test
    public void testCreateSuccess() throws InvalidVertexException {
        assertEquals(new Vertex('e', 4), Vertex.create("e4"));
    }

    @Test(expected = InvalidVertexException.class)
    public void testCreateFailureNull() throws InvalidVertexException {
        Vertex.create(null);
    }

    @Test(expected = InvalidVertexException.class)
    public void testCreateFailureRowNotANumber() throws InvalidVertexException {
        Vertex.create("ez");
    }

    @Test(expected = InvalidVertexException.class)
    public void testCreateFailureNotTwoChars() throws InvalidVertexException {
        Vertex.create("hello");
    }

    @Test(expected = InvalidVertexException.class)
    public void testCreateFailureNotValid() throws InvalidVertexException {
        Vertex.create("j9");
    }

    @Test
    public void testEqualsHashCodeToString() {
        final char col_e = 'e';
        final char col_d = 'd';
        final int row_4 = 4;
        final int row_5 = 5;
        final String value_e4 = "e4";
        Vertex e4_1 = new Vertex(col_e, row_4);
        Vertex e4_2 = new Vertex(col_e, row_4);
        Vertex d4 = new Vertex(col_d, row_4);
        Vertex e5 = new Vertex(col_e, row_5);
        assertEquals(e4_1, e4_1);
        assertFalse(e4_1.equals(value_e4));
        assertEquals(e4_1, e4_2);
        assertFalse(e4_1.equals(d4));
        assertFalse(e4_1.equals(e5));
        assertEquals(e4_1.hashCode(), e4_2.hashCode());
        assertEquals(value_e4, e4_1.toString());
    }

    @Test
    public void testValid() {
        final char COL_VALID = 'e';
        final char COL_INVALID_BEFORE = 'A';
        final char COL_INVALID_AFTER = 'z';
        final int ROW_VALID = 4;
        final int ROW_INVALID_BEFORE = 0;
        final int ROW_INVALID_AFTER = 9;

        final Vertex valid = new Vertex(COL_VALID, ROW_VALID);
        final Vertex invalidColBefore = new Vertex(COL_INVALID_BEFORE, ROW_VALID);
        final Vertex invalidColAfter = new Vertex(COL_INVALID_AFTER, ROW_VALID);
        final Vertex invalidRowBefore = new Vertex(COL_VALID, ROW_INVALID_BEFORE);
        final Vertex invalidRowAfter = new Vertex(COL_VALID, ROW_INVALID_AFTER);

        assertTrue(valid.isValid());
        assertFalse(invalidColBefore.isValid());
        assertFalse(invalidColAfter.isValid());
        assertFalse(invalidRowBefore.isValid());
        assertFalse(invalidRowAfter.isValid());

        assertTrue(valid.isValidCol());
        assertFalse(invalidColBefore.isValidCol());
        assertFalse(invalidColAfter.isValidCol());

        assertTrue(valid.isValidRow());
        assertFalse(invalidRowBefore.isValidRow());
        assertFalse(invalidRowAfter.isValidRow());
    }

    @Test
    public void testNavigation() {
        Vertex source = new Vertex('e', 4);
        assertEquals(new Vertex('e', 5), source.go(Direction.NORTH));
        assertEquals(new Vertex('e', 3), source.go(Direction.SOUTH));
        assertEquals(new Vertex('d', 4), source.go(Direction.WEST));
        assertEquals(new Vertex('f', 4), source.go(Direction.EAST));
        assertEquals(new Vertex('d', 5), source.go(Direction.NORTH_WEST));
        assertEquals(new Vertex('f', 5), source.go(Direction.NORTH_EAST));
        assertEquals(new Vertex('d', 3), source.go(Direction.SOUTH_WEST));
        assertEquals(new Vertex('f', 3), source.go(Direction.SOUTH_EAST));
    }

    @Test
    public void testNumberedRows() {
        Vertex white3 = new Vertex('e', 3);
        Vertex white4 = new Vertex('e', 4);
        Vertex white8 = new Vertex('e', 8);
        Vertex black3 = new Vertex('e', 6);
        Vertex black4 = new Vertex('e', 5);
        Vertex black8 = new Vertex('e', 1);
        assertTrue(white3.onThirdRow(Color.WHITE));
        assertTrue(black3.onThirdRow(Color.BLACK));
        assertTrue(white4.onFourthRow(Color.WHITE));
        assertTrue(black4.onFourthRow(Color.BLACK));
        assertTrue(white8.onLastRow(Color.WHITE));
        assertTrue(black8.onLastRow(Color.BLACK));
        assertFalse(white3.onFourthRow(Color.WHITE));
        assertFalse(black3.onFourthRow(Color.BLACK));
        assertFalse(white4.onThirdRow(Color.WHITE));
        assertFalse(black4.onThirdRow(Color.BLACK));
        assertFalse(white3.onLastRow(Color.WHITE));
        assertFalse(black4.onLastRow(Color.BLACK));
    }
}
