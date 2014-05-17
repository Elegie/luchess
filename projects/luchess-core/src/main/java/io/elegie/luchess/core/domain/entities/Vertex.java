package io.elegie.luchess.core.domain.entities;

import io.elegie.luchess.core.domain.exceptions.InvalidVertexException;

/**
 * A Vertex represents a square of the board, and is referenced by a set of two
 * coordinates (row and column indices). Thus, 'e4', 'h8', 'a5' are vertices.
 */
@SuppressWarnings("javadoc")
public class Vertex {

    public static final char START_COL = 'a';
    public static final char END_COL = 'h';
    public static final int START_ROW = 1;
    public static final int END_ROW = 8;

    public static final char QUEEN_ROOK_COL = 'a';
    public static final char QUEEN_KNIGHT_COL = 'b';
    public static final char QUEEN_BISHOP_COL = 'c';
    public static final char QUEEN_COL = 'd';
    public static final char KING_COL = 'e';
    public static final char KING_BISHOP_COL = 'f';
    public static final char KING_KNIGHT_COL = 'g';
    public static final char KING_ROOK_COL = 'h';

    public static final int FIRST_WHITE_ROW = 1;
    public static final int FIRST_BLACK_ROW = 8;
    public static final int SECOND_WHITE_ROW = 2;
    public static final int SECOND_BLACK_ROW = 7;
    public static final int THIRD_WHITE_ROW = 3;
    public static final int THIRD_BLACK_ROW = 6;
    public static final int FOURTH_WHITE_ROW = 4;
    public static final int FOURTH_BLACK_ROW = 5;

    private char col = 0;
    private int row = 0;

    public Vertex(char col, int row) {
        this.col = col;
        this.row = row;
    }

    public char getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    /**
     * A Vertex equals another vertex when their coordinates are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Vertex)) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return vertex.col == this.col && vertex.row == this.row;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int start = 17;
        int result = start;
        result = prime * result + (col);
        result = prime * result + row;
        return result;
    }

    @Override
    public String toString() {
        return Character.toString(col) + Integer.toString(row);
    }

    // --- Validators ---------------------------------------------------------

    /**
     * @return True when the coordinates of the vertex (row and column) are
     *         valid.
     */
    public boolean isValid() {
        return isValidCol() && isValidRow();
    }

    /**
     * @return True when the column is between authorized values ('a' to 'h').
     */
    public boolean isValidCol() {
        return col >= START_COL && col <= END_COL;
    }

    /**
     * @return True when the row is between authorized values ('1' to '8').
     */
    public boolean isValidRow() {
        return row >= START_ROW && row <= END_ROW;
    }

    // --- Navigator ----------------------------------------------------------

    /**
     * Finds an adjacent vertex from the current vertex, given a direction.
     * 
     * @param direction
     *            Where we are looking for.
     * @return A new Vertex, adjacent to the current vertex, and located in the
     *         searched direction.
     */
    public Vertex go(Direction direction) {
        return new Vertex((char) (col + direction.getColOffset()), row + direction.getRowOffset());
    }

    public boolean onThirdRow(Color color) {
        return onGivenRow(color, THIRD_WHITE_ROW, THIRD_BLACK_ROW);
    }

    public boolean onFourthRow(Color color) {
        return onGivenRow(color, FOURTH_WHITE_ROW, FOURTH_BLACK_ROW);
    }

    public boolean onLastRow(Color color) {
        return onGivenRow(color, FIRST_BLACK_ROW, FIRST_WHITE_ROW);
    }

    private boolean onGivenRow(Color color, int rowWhite, int rowBlack) {
        if (Color.WHITE.equals(color)) {
            return getRow() == rowWhite;
        }
        if (Color.BLACK.equals(color)) {
            return getRow() == rowBlack;
        }
        String message = "Unrecognized color (%s).";
        message = String.format(message, color);
        throw new IllegalArgumentException(message);
    }

    // ------------------------------------------------------------------------

    /**
     * @param value
     *            The text representation of a valid vertex.
     * @return The vertex created from its text representation.
     * @throws InvalidVertexException
     *             When the text representation cannot be parsed into a vertex.
     */
    public static Vertex create(String value) throws InvalidVertexException {
        if (value != null && value.length() == 2) {
            char col = value.charAt(0);
            int row = 0;
            try {
                row = Integer.parseInt(Character.toString(value.charAt(1)));
            } catch (NumberFormatException e) {
                String message = "Vertex row is not a number (%s).";
                message = String.format(message, value);
                throw new InvalidVertexException(message, e);
            }
            Vertex result = new Vertex(col, row);
            if (result.isValid()) {
                return result;
            }
        }
        String message = "Vertex cannot be created from value (%s).";
        message = String.format(message, value);
        throw new InvalidVertexException(message);
    }

}
