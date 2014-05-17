package io.elegie.luchess.core.domain.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * This class acts as an accumulator for text moves.
 */
public class MoveText {

    /**
     * Separator character used to separate moves in the final text
     * representation: {@value #SEPARATOR}.
     */
    public static final char SEPARATOR = ' ';

    private List<String> valueBuilder = new LinkedList<>();

    /**
     * @return True when no move has been added yet.
     */
    public boolean isEmpty() {
        return valueBuilder.isEmpty();
    }

    /**
     * @return The list of moves, separated by {@link #SEPARATOR}.
     */
    public String getValue() {
        StringBuilder accumulator = new StringBuilder();
        boolean first = true;
        for (String value : valueBuilder) {
            if (!first) {
                accumulator.append(SEPARATOR);
            }
            first = false;
            accumulator.append(value);
        }
        return accumulator.toString();
    }

    /**
     * @param value
     *            The move to be accumulated.
     */
    public void addValue(String value) {
        valueBuilder.add(value);
    }

    /**
     * A MoveText equals another text when their values are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MoveText)) {
            return false;
        }
        MoveText moveText = (MoveText) o;
        return valueBuilder.equals(moveText.valueBuilder);
    }

    @Override
    public int hashCode() {
        return valueBuilder.hashCode();
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {values: %s}";
        return String.format(value, className, valueBuilder);
    }
}
