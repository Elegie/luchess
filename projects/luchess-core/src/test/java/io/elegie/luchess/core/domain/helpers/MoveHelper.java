package io.elegie.luchess.core.domain.helpers;

import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;

/**
 * Helpers providing methods to create moves.
 */
public final class MoveHelper {

    private MoveHelper() {

    }

    /**
     * Creates and initializes a simple pawn forward move.
     * 
     * @param col
     *            Column of the target vertex.
     * @param row
     *            Row of the target vertex.
     * @return A PawnForwardMove initialized with the target vertex.
     */
    public static PawnForwardMove createPawnForwardMove(char col, int row) {
        PawnForwardMove move = new PawnForwardMove();
        move.setTarget(new Vertex(col, row));
        return move;
    }

}
