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
 * This class represents a directional move: the piece is expected to move along
 * one or many squares, in the same direction (horizontal, vertical or
 * diagonal).
 */
public abstract class AbstractFigurineDirectionalMove extends AbstractFigurineMove {

    @SuppressWarnings("javadoc")
    public AbstractFigurineDirectionalMove(Figurine figurine) {
        super(figurine);
    }

    /**
     * Implementations should provide all directions to be explored, starting
     * from the target.
     */
    protected abstract Direction[] getDirections();

    /**
     * Finds a list of candidate sources, by reading all directions from the
     * target, and registering all pieces of the correct type and color.
     */
    @Override
    protected List<Vertex> getPossibleSources(Color color, Position position) {
        List<Vertex> candidates = new ArrayList<>();
        Direction[] directions = getDirections();
        for (Direction dir : directions) {
            Vertex candidate = getTarget();
            do {
                candidate = candidate.go(dir);
            } while (candidate.isValid() && position.getPiece(candidate) == null);
            if (candidate.isValid() && Piece.compare(position.getPiece(candidate), getFigurine(), color)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }
}
