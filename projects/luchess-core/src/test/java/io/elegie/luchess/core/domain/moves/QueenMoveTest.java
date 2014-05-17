package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.LinkedList;
import java.util.List;

/**
 * A queen moves in any direction (like a rook or a bishop).
 */
public class QueenMoveTest extends AbstractFigurineMoveTest {

    @Override
    protected Class<? extends AbstractFigurineMove> getFigurineMove() {
        return QueenMove.class;
    }

    @Override
    protected List<Vertex> getValidTargets() {
        BishopMoveTest bishopMoveTest = new BishopMoveTest();
        RookMoveTest rookMoveTest = new RookMoveTest();
        List<Vertex> validTargets = new LinkedList<>();
        validTargets.addAll(bishopMoveTest.getValidTargets());
        validTargets.addAll(rookMoveTest.getValidTargets());
        return validTargets;
    }
}
