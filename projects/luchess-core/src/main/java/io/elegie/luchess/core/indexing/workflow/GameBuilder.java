package io.elegie.luchess.core.indexing.workflow;

import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.core.domain.entities.Result;
import io.elegie.luchess.pgn.api.AbstractParserVisitor;
import io.elegie.luchess.pgn.impl.tokens.TagPairNameToken;

/**
 * A simple game visitor, which can be used to obtain a game from a PGN text
 * stream. The fields retrieved from the game are the ones described in the
 * {@link Game}.
 */
@SuppressWarnings("javadoc")
public class GameBuilder extends AbstractParserVisitor {

    private Game game;
    private MoveText moveText;
    private String tempFieldName;

    public GameBuilder() {
        game = new Game();
        moveText = game.getMoveText();
        tempFieldName = null;
    }

    public Game getGame() {
        return game;
    }

    /**
     * @return Whether the game can be retrieved from the builder.
     */
    public boolean isComplete() {
        return game.getResult() != null && !moveText.isEmpty();
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {game: %s, moveText: %s}";
        return String.format(value, className, game, moveText);
    }

    // --- PGN Parser Visitor Implementation ----------------------------------

    @Override
    public void visitTagPairName(String value) {
        tempFieldName = value;
    }

    @Override
    public void visitTagPairValue(String value) {
        if (tempFieldName != null && value != null && !tempFieldName.isEmpty() && !value.isEmpty()) {
            if (tempFieldName.equalsIgnoreCase(TagPairNameToken.WHITE)) {
                game.setWhite(value);
            }

            if (tempFieldName.equalsIgnoreCase(TagPairNameToken.BLACK)) {
                game.setBlack(value);
            }

            if (tempFieldName.equalsIgnoreCase(TagPairNameToken.WHITE_ELO)) {
                game.setWhiteElo(Integer.parseInt(value));
            }

            if (tempFieldName.equalsIgnoreCase(TagPairNameToken.BLACK_ELO)) {
                game.setBlackElo(Integer.parseInt(value));
            }
        }
        tempFieldName = null;
    }

    @Override
    public void visitUnstructuredMove(String value) {
        moveText.addValue(value);
    }

    @Override
    public void visitTermination(String value) {
        game.setResult(Result.parse(value));
    }

}
