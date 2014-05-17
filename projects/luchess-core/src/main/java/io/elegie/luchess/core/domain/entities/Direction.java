package io.elegie.luchess.core.domain.entities;

/**
 * This reflects directions on a board, with the White player being South, and
 * the Black player being North. Directions are used to help qualify piece
 * moves, and determine whether a move is possible in a given position.
 */
@SuppressWarnings("javadoc")
public enum Direction {

    NORTH(0, 1),
    WEST(-1, 0),
    SOUTH(0, -1),
    EAST(1, 0),
    NORTH_WEST(-1, 1),
    NORTH_EAST(1, 1),
    SOUTH_WEST(-1, -1),
    SOUTH_EAST(1, -1);

    private int colOffset;
    private int rowOffset;

    private Direction(int colOffset, int rowOffset) {
        this.colOffset = colOffset;
        this.rowOffset = rowOffset;
    }

    public int getColOffset() {
        return colOffset;
    }

    public int getRowOffset() {
        return rowOffset;
    }
}
