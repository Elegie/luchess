package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Color;
import io.elegie.luchess.core.domain.entities.Direction;
import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Piece;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.pgn.impl.tokens.PromotionToken;

/**
 * Provides common behaviors for pawn moves (promotion and value).
 */
@SuppressWarnings("javadoc")
public abstract class AbstractPawnMove extends AbstractFigurineMove {

    private Figurine promotedFigurine;

    public AbstractPawnMove() {
        super(Figurine.PAWN);
    }

    public void setPromotion(Figurine promotedFigurine) {
        this.promotedFigurine = promotedFigurine;
    }

    public Figurine getPromotion() {
        return promotedFigurine;
    }

    @Override
    public void apply(Color color, Position position) throws IllegalMoveException {
        super.apply(color, position);
        applyPromote(color, position);
    }

    @Override
    public boolean canApply(Color color, Position position) {
        return super.canApply(color, position) && checkPromote(color);
    }

    protected String getPromotionValue() {
        StringBuilder value = new StringBuilder();
        if (promotedFigurine != null) {
            value.append(PromotionToken.PROMOTION).append(promotedFigurine.toString());
        }
        return value.toString();
    }

    protected Direction getBackDirection(Color color) {
        return color.equals(Color.WHITE) ? Direction.SOUTH : Direction.NORTH;
    }

    private void applyPromote(Color color, Position position) {
        if (promotedFigurine != null) {
            position.setPiece(getTarget(), new Piece(promotedFigurine, color));
        }
    }

    private boolean checkPromote(Color color) {
        boolean hasFigurine = promotedFigurine != null;
        boolean shouldPromote = getTarget().onLastRow(color);
        boolean checkPromoteFigurine = shouldPromote && hasFigurine
                && !(promotedFigurine.equals(Figurine.PAWN) || promotedFigurine.equals(Figurine.KING));
        boolean checkNotPromote = !(shouldPromote || hasFigurine);
        return checkPromoteFigurine || checkNotPromote;
    }
}
