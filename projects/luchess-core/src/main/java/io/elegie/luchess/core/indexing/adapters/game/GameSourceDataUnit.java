package io.elegie.luchess.core.indexing.adapters.game;

import io.elegie.luchess.core.api.build.SourceDataUnit;
import io.elegie.luchess.core.domain.entities.Game;

/**
 * A data unit made around one game.
 */
public class GameSourceDataUnit implements SourceDataUnit {

    private Game game;
    private boolean iterated = false;

    /**
     * @param game
     *            Game to be wrapped by the data unit.
     */
    public GameSourceDataUnit(Game game) {
        this.game = game;
    }

    @Override
    public Game next() {
        if (!iterated) {
            iterated = true;
            return game;
        }
        return null;
    }

    @Override
    public void beforeProcessing() {
        // Nothing
    }

    @Override
    public void afterProcessing(boolean success) {
        // Nothing
    }

}
