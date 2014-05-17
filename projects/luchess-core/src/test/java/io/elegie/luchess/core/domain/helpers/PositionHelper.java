package io.elegie.luchess.core.domain.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;

/**
 * Helpers providing methods to validate end positions.
 */
public final class PositionHelper {

    private PositionHelper() {

    }

    /**
     * Validate that a position contains only one piece, at a given vertex.
     * 
     * @param position
     *            The position to be validated.
     * @param target
     *            Where the piece is expected to be (the rest being empty).
     * @param expected
     *            Which piece.
     */
    public static void validateEndPosition(Position position, Vertex target, Piece expected) {
        for (int row = Vertex.START_ROW; row <= Vertex.END_ROW; row++) {
            for (char col = Vertex.START_COL; col <= Vertex.END_COL; col++) {
                Vertex current = new Vertex(col, row);
                Piece piece = position.getPiece(current);
                if (current.equals(target)) {
                    assertEquals(expected, piece);
                } else {
                    assertNull(piece);
                }
            }
        }
    }

}
