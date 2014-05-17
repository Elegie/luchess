package io.elegie.luchess.core.domain.entities;

/**
 * A piece is a combination of a figurine and a color, such as a White Pawn
 * ("WP"), or a Black Queen ("BQ").
 */
@SuppressWarnings("javadoc")
public class Piece {

    private static final Piece[] PIECES = new Piece[Color.values().length * Figurine.values().length];
    static {
        int ii = 0;
        for (Color color : Color.values()) {
            for (Figurine figurine : Figurine.values()) {
                PIECES[ii] = new Piece(figurine, color);
                ii++;
            }
        }
    }

    private final Figurine figurine;
    private final Color color;

    public Piece(Figurine figurine, Color color) {
        this.figurine = figurine;
        this.color = color;
    }

    public Figurine getFigurine() {
        return figurine;
    }

    public Color getColor() {
        return color;
    }

    /**
     * A piece equals another piece if it is the same figurine and the same
     * color.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }
        Piece piece = (Piece) o;
        return piece.figurine == this.figurine && piece.color == this.color;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * this.figurine.hashCode() + this.color.hashCode();
    }

    @Override
    public String toString() {
        return color.toString() + figurine.toString();
    }

    /**
     * @param pieceValue
     *            The piece value to be parsed into a piece (e.g. "WQ" for
     *            "White Queen"). The value of the piece is the concatenation of
     *            the values of the color and the figurine.
     * @return The parsed piece, or null if the value could not be parsed.
     */
    public static Piece parse(String pieceValue) {
        for (int ii = 0; ii < PIECES.length; ii++) {
            if (PIECES[ii].toString().equals(pieceValue)) {
                return PIECES[ii];
            }
        }
        return null;
    }

    /**
     * Checks that a piece has the right figurine and color.
     * 
     * @param piece
     *            The piece to be checked.
     * @param figurine
     *            The expected figurine.
     * @param color
     *            The expected color.
     * @return True when the piece exists and has the expected figurine and
     *         color.
     */
    public static boolean compare(Piece piece, Figurine figurine, Color color) {
        return piece != null && piece.getFigurine().equals(figurine) && piece.getColor().equals(color);
    }
}
