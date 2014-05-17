package io.elegie.luchess.core.domain.entities;

import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

import java.util.List;

/**
 * This class represents a chess board. It wraps a {@link Position}, and
 * provides helper methods to access and/or update it.
 */
public class Board {

    private Position position;

    /**
     * Constructs a new board, using a standard starting chess position.
     */
    public Board() {
        position = new Position();
        position.init();
    }

    /**
     * @param col
     *            Which column to look at.
     * @param row
     *            Which row to look at.
     * @return the {@link Piece} located at the coordinates (column, row).
     */
    public Piece getPiece(char col, int row) {
        return position.getPiece(new Vertex(col, row));
    }

    /**
     * Helper method which plays a list of moves upon the contained
     * {@link Position}.
     * 
     * @param moves
     *            The moves to be applied onto the position, always starting
     *            with the first move of the game.
     * @throws IllegalMoveException
     *             When the move is not valid in regards of the position.
     */
    public void play(List<Move> moves) throws IllegalMoveException {
        int colorIndex = 0;
        for (Move move : moves) {
            Color color = (colorIndex % 2 == 0) ? Color.WHITE : Color.BLACK;
            move.apply(color, position);
            colorIndex++;
        }
    }

    @Override
    public String toString() {
        String value = "%s {position: %s}";
        return String.format(value, this.getClass().getSimpleName(), position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        Board board = (Board) o;
        return position.equals(board.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

}
