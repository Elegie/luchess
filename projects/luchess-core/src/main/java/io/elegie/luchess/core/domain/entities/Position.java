package io.elegie.luchess.core.domain.entities;

import io.elegie.luchess.core.domain.exceptions.InvalidPositionException;

import java.util.HashMap;
import java.util.Map;

/**
 * The position keeps state of all {@link Piece}s on board. It associates each
 * {@link Vertex} of the board (i.e. a square, such as 'e4') with the piece
 * being on it (or null if the square is empty).
 */
public class Position {

    private static final Figurine[] FIGURINES = new Figurine[] { Figurine.ROOK, Figurine.KNIGHT, Figurine.BISHOP,
            Figurine.QUEEN, Figurine.KING, Figurine.BISHOP, Figurine.KNIGHT, Figurine.ROOK };

    private Map<Vertex, Piece> position = new HashMap<>();

    /**
     * Creates a position. By default, the position is empty - use the
     * {@link #init()} if you want to populate a standard starting position.
     */
    public Position() {
        for (int row = Vertex.START_ROW; row <= Vertex.END_ROW; row++) {
            for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
                position.put(new Vertex(col, row), null);
            }
        }
    }

    /**
     * Removes all pieces from the board.
     */
    public void clear() {
        for (Vertex vertex : position.keySet()) {
            position.put(vertex, null);
        }
    }

    /**
     * Sets up a starting position, positioning the pieces according to
     * traditional chess rules. Subclasses could define another set of starting
     * pieces if needed.
     */
    public void init() {
        clear();
        int colStart = Vertex.START_COL;
        for (int ii = 0; ii < FIGURINES.length; ii++) {
            char colCurrent = (char) (colStart + ii);
            position.put(new Vertex(colCurrent, Vertex.START_ROW), new Piece(FIGURINES[ii], Color.WHITE));
            position.put(new Vertex(colCurrent, Vertex.END_ROW), new Piece(FIGURINES[ii], Color.BLACK));
        }
        Piece whitePawn = new Piece(Figurine.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(Figurine.PAWN, Color.BLACK);
        for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
            position.put(new Vertex(col, Vertex.START_ROW + 1), whitePawn);
            position.put(new Vertex(col, Vertex.END_ROW - 1), blackPawn);
        }
    }

    /**
     * Positions a piece on a given vertex. This is not a move - the piece will
     * simply go on a square, replacing any existing piece.
     * 
     * @param vertex
     *            The target square.
     * @param piece
     *            The new piece.
     */
    public void setPiece(Vertex vertex, Piece piece) {
        position.put(vertex, piece);
    }

    /**
     * Retrieves the piece from a given square.
     * 
     * @param vertex
     *            The target square.
     * @return The piece located on the vertex, or null if the vertex is empty,
     *         missing or invalid.
     */
    public Piece getPiece(Vertex vertex) {
        return position.get(vertex);
    }

    // --- equals, hashcode() and toString() ----------------------------------

    /**
     * A position equals another if it has the same disposition of pieces.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position candidate = (Position) o;
        for (Vertex vertex : position.keySet()) {
            Piece piece = getPiece(vertex);
            Piece candidatePiece = candidate.getPiece(vertex);
            if (piece == null && candidatePiece != null || piece != null && !piece.equals(candidatePiece)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int start = 17;
        int result = start;
        for (Vertex vertex : position.keySet()) {
            Piece piece = getPiece(vertex);
            if (piece != null) {
                result = prime * result + piece.hashCode();
            }
        }
        return result;
    }

    /**
     * An ASCII representation of the position, mostly for debugging purposes.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append('\n');
        for (int row = Vertex.END_ROW; row >= Vertex.START_ROW; row--) {
            builder.append(row);
            builder.append('|');
            for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
                Piece piece = position.get(new Vertex(col, row));
                builder.append(piece == null ? ".." : piece.toString());
                if (col != Vertex.END_COL) {
                    builder.append(' ');
                }
            }
            builder.append('\n');
        }
        builder.append("  ");
        for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
            builder.append("--");
            if (col != Vertex.END_COL) {
                builder.append(' ');
            }
        }
        builder.append('\n');
        builder.append("  ");
        for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
            builder.append(' ');
            builder.append(col);
            if (col != Vertex.END_COL) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }

    // --- Serialization ------------------------------------------------------

    /**
     * The separator character to be used to delimit squares in the string
     * representation of a position: {@value #POSITION_SERIALIZATION_SEPARATOR}.
     * See the {@link #deserialize(String)} method for an explanation of the
     * serialized text format.
     */
    public static final String POSITION_SERIALIZATION_SEPARATOR = ";";

    /**
     * Transforms a position object into a string representation of the
     * position.
     * 
     * @param position
     *            The position to be serialized.
     * @return The string representation of the position.
     */
    public static String serialize(Position position) {
        StringBuilder representation = new StringBuilder();
        boolean first = true;
        for (int row = Vertex.END_ROW; row >= Vertex.START_ROW; row--) {
            for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
                if (!first) {
                    representation.append(POSITION_SERIALIZATION_SEPARATOR);
                }
                first = false;
                Piece piece = position.getPiece(new Vertex(col, row));
                representation.append(piece == null ? "" : piece.toString());
            }
        }
        return representation.toString();
    }

    /**
     * Builds a position object from a string representation. The representation
     * lists the content of each square of the board, starting from the top left
     * (a8) and ending to the bottom right (h1), the content being the piece
     * string representation, or the empty string. Each square is separated by
     * the {@value #POSITION_SERIALIZATION_SEPARATOR}.
     * 
     * Example: BR;BN;BB;...WB;WN;WR.
     * 
     * @param representation
     *            The string representation of a position.
     * @return A position object.
     * @throws InvalidPositionException
     *             When the position cannot be built from the passed
     *             representation.
     */
    public static Position deserialize(String representation) throws InvalidPositionException {
        if (representation == null || representation.isEmpty()) {
            String message = "The representation must not be null or empty.";
            throw new InvalidPositionException(message);
        }

        String[] pieces = representation.split(POSITION_SERIALIZATION_SEPARATOR, -1);
        int boardSize = FIGURINES.length;
        int numSquares = boardSize * boardSize;
        if (pieces.length != numSquares) {
            String message = "The representation has %s squares, while %s are expected.";
            message = String.format(message, pieces.length, numSquares);
            throw new InvalidPositionException(message);
        }

        Position position = new Position();
        for (int ii = 0; ii < numSquares; ii++) {
            if (!pieces[ii].isEmpty()) {
                char col = (char) (Vertex.START_COL + (ii % boardSize));
                int row = boardSize - (ii / boardSize);
                Vertex vertex = new Vertex(col, row);
                Piece piece = Piece.parse(pieces[ii]);
                if (piece != null) {
                    position.setPiece(vertex, piece);
                }
            }
        }
        return position;
    }

}
