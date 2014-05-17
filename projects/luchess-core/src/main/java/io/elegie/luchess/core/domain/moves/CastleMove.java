package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;

/**
 * <p>
 * A castle is special move, by which the king and one of its related rook are
 * moved at the same time. There are two types of castles: the small one "O-O",
 * which happens to the east of the board, and the large one "O-O-O", which
 * happens to the west of the board. For each of these, the target positions of
 * the king and of the rook are preset.
 * </p>
 * 
 * <p>
 * Legally, a castle may happen only if neither the king nor the rook have
 * already moved, or if the path from the respective original squares to the
 * target squares neither is checked by the opponent, nor contains any other
 * piece. Of these three rules, this implementation only checks that the path is
 * free of any obstacle.
 * </p>
 */
public class CastleMove extends AbstractMove {

    private static final String SMALL_CASTLE = "O-O";
    private static final String LARGE_CASTLE = "O-O-O";

    private boolean isSmall;

    /**
     * @param isSmall
     *            Specify whether we make a small castle or a large one.
     */
    public CastleMove(boolean isSmall) {
        this.isSmall = isSmall;
    }

    @Override
    public String getUncheckedValue() {
        return isSmall ? SMALL_CASTLE : LARGE_CASTLE;
    }

    @Override
    public void apply(Color color, Position position) throws IllegalMoveException {
        if (canApply(color, position)) {
            position.setPiece(getOriginKingSquare(color), null);
            position.setPiece(getOriginRookSquare(color), null);
            position.setPiece(getTargetKingSquare(color), new Piece(Figurine.KING, color));
            position.setPiece(getTargetRookSquare(color), new Piece(Figurine.ROOK, color));
        } else {
            String message = "Cannot apply move %s for color %s onto following position: %s";
            message = String.format(message, this, color, position);
            throw new IllegalMoveException(message);
        }
    }

    @Override
    public boolean canApply(Color color, Position position) {
        if (!checkDisambiguation(color) || !checkTarget(color)) {
            return false;
        }
        Vertex throne = getOriginKingSquare(color);
        Piece king = position.getPiece(throne);
        if (Piece.compare(king, Figurine.KING, color)) {
            Vertex tower = fromThroneToTower(position, throne);
            if (isRookCol(tower.getCol())) {
                Piece supposedRook = position.getPiece(tower);
                if (Piece.compare(supposedRook, Figurine.ROOK, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDisambiguation(Color color) {
        Vertex disambiguation = getDisambiguation();
        return disambiguation == null || getOriginKingSquare(color).equals(disambiguation);
    }

    private boolean checkTarget(Color color) {
        Vertex target = getTarget();
        return target == null || getTargetKingSquare(color).equals(target);
    }

    // --- King and Rook origin and target ------------------------------------

    private Direction getCastleDirection() {
        return isSmall ? Direction.EAST : Direction.WEST;
    }

    private Direction getOppositeCastleDirection() {
        return isSmall ? Direction.WEST : Direction.EAST;
    }

    private Vertex getOriginKingSquare(Color color) {
        return new Vertex(Vertex.KING_COL, getCastleRow(color));
    }

    private Vertex getOriginRookSquare(Color color) {
        return new Vertex(getCastleRookCol(), getCastleRow(color));
    }

    private Vertex getTargetKingSquare(Color color) {
        return getOriginKingSquare(color).go(getCastleDirection()).go(getCastleDirection());
    }

    private Vertex getTargetRookSquare(Color color) {
        return getTargetKingSquare(color).go(getOppositeCastleDirection());
    }

    // --- Vertex calculators -------------------------------------------------

    private Vertex fromThroneToTower(Position position, Vertex throne) {
        Vertex current = throne;
        do {
            current = current.go(getCastleDirection());
        } while (current.isValid() && position.getPiece(current) == null);
        return current;
    }

    // --- Vertex Helpers -----------------------------------------------------

    private int getCastleRow(Color color) {
        return color.equals(Color.WHITE) ? Vertex.START_ROW : Vertex.END_ROW;
    }

    private char getCastleRookCol() {
        return isSmall ? Vertex.KING_ROOK_COL : Vertex.QUEEN_ROOK_COL;
    }

    private boolean isRookCol(char col) {
        return col == Vertex.KING_ROOK_COL || col == Vertex.QUEEN_ROOK_COL;
    }

}
