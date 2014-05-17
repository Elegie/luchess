package io.elegie.luchess.core.domain.entities;

/**
 * Color played by a player, i.e. one of WHITE or BLACK.
 */
public enum Color {

    /**
     * Color White: "W".
     */
    WHITE('W'),

    /**
     * Color Black: "B".
     */
    BLACK('B');

    private char value;

    private Color(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Character.toString(value);
    }

    /**
     * Helper method, which inverts the color, in order to provide more readable
     * algorithms.
     * 
     * @return Black becomes White, and vice-versa.
     */
    public Color invert() {
        return this.equals(WHITE) ? BLACK : WHITE;
    }

    /**
     * @param colorValue
     *            The value to be parsed into a column.
     * @return The parsed color, or null if the value could not be parsed.
     */
    public static Color parse(char colorValue) {
        for (Color color : values()) {
            if (colorValue == color.value) {
                return color;
            }
        }
        return null;
    }

}
