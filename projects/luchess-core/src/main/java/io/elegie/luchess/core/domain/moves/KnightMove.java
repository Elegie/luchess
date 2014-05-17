package io.elegie.luchess.core.domain.moves;

import static io.elegie.luchess.core.domain.entities.Direction.EAST;
import static io.elegie.luchess.core.domain.entities.Direction.NORTH;
import static io.elegie.luchess.core.domain.entities.Direction.SOUTH;
import static io.elegie.luchess.core.domain.entities.Direction.WEST;
import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * A knight moves two moves in a given direction, then one move in a direction
 * perpendicular to the initial direction.
 */
public class KnightMove extends AbstractFigurineMove {

    @SuppressWarnings("javadoc")
    public KnightMove() {
        super(Figurine.KNIGHT);
    }

    @Override
    protected List<Vertex> getPossibleSources(Color color, Position position) {
        Direction[] first = new Direction[] { NORTH, NORTH, EAST, EAST, SOUTH, SOUTH, WEST, WEST };
        Direction[] second = new Direction[] { EAST, WEST, NORTH, SOUTH, EAST, WEST, NORTH, SOUTH };
        List<Vertex> candidates = new ArrayList<>();
        Vertex target = getTarget();
        Figurine figurine = getFigurine();
        for (int ii = 0; ii < first.length; ii++) {
            Vertex candidate = target.go(first[ii]).go(first[ii]).go(second[ii]);
            if (candidate.isValid() && Piece.compare(position.getPiece(candidate), figurine, color)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }
}
