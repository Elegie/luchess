package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * A king can move in any direction, but only by one square.
 */
public class KingMove extends AbstractFigurineMove {

    @SuppressWarnings("javadoc")
    public KingMove() {
        super(Figurine.KING);
    }

    @Override
    protected List<Vertex> getPossibleSources(Color color, Position position) {
        List<Vertex> candidates = new ArrayList<>();
        Vertex target = getTarget();
        for (Direction dir : Direction.values()) {
            Vertex candidate = target.go(dir);
            if (candidate.isValid() && Piece.compare(position.getPiece(candidate), getFigurine(), color)) {
                candidates.add(candidate);
                break;
            }
        }
        return candidates;
    }

}
