package io.elegie.luchess.core.domain.entities;

import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

/**
 * Represents a move by a player, such as 'e4'. Implementations are expected to
 * provide the algorithm to effectively apply a move on a given {@link Position}
 * , for a target {@link Color}.
 */
public abstract class Move {

    /**
     * Plays the move on a given {@link Position}, for a certain {@link Color}.
     * Note that in the PGN specification, the color is not indicated in the
     * move itself, but rather derived from the sequence of moves (White being
     * the first one to play).
     * 
     * Given an immutable move, implementations should make sure that this
     * method remains thread-safe. In other words, the same move object should
     * be concurrently applicable to different positions.
     * 
     * @param color
     *            Color of the player playing the move.
     * @param position
     *            Position upon which the move is played.
     * @throws IllegalMoveException
     *             When the current position does not allow the move. For
     *             instance, the move may refer to some invalid square (e.g. an
     *             empty square, while we would expect to find the piece we want
     *             to move or capture).
     */
    public abstract void apply(Color color, Position position) throws IllegalMoveException;

    /**
     * Checks whether a move can be played on a given position, for a given
     * {@link Color}.
     * 
     * @param color
     *            Color of the player about to play the move.
     * @param position
     *            Position upon which the move should be played.
     * @return True when the move can safely be applied, false otherwise.
     *         Further trying to apply a move upon a position which does not
     *         allow it will generate a {@link IllegalMoveException}.
     */
    public abstract boolean canApply(Color color, Position position);

    /**
     * @return The text representation of the Move. We use the PGN convention.
     */
    public abstract String getValue();

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {value: %s}";
        return String.format(value, className, getValue());
    }

    /**
     * A move equals another if it has the same value, regardless of the color
     * or the position.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Move)) {
            return false;
        }
        Move move = (Move) o;
        return getValue().equals(move.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

}
