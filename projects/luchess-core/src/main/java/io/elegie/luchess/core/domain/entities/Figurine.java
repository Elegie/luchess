package io.elegie.luchess.core.domain.entities;

/**
 * Enumeration of all figurine pieces, including pawns. Figurines are used to
 * qualify moves (piece being moved, piece being promoted).
 */
public enum Figurine {

    /**
     * A pawn: "P".
     */
    PAWN('P'),

    /**
     * A rook: "R".
     */
    ROOK('R'),

    /**
     * A knight: "N".
     */
    KNIGHT('N'),

    /**
     * A bishop: "B".
     */
    BISHOP('B'),

    /**
     * A queen: "Q".
     */
    QUEEN('Q'),

    /**
     * A king: "K".
     */
    KING('K');

    private char value;

    private Figurine(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Character.toString(value);
    }

    /**
     * Returns a Figurine object from its char value.
     * 
     * @param figurineValue
     *            Char representing the figurine to be parsed (a valid PGN
     *            token).
     * @return The corresponding Figurine object.
     */
    public static Figurine parse(char figurineValue) {
        for (Figurine figurine : values()) {
            if (figurineValue == figurine.value) {
                return figurine;
            }
        }
        return null;
    }

}
