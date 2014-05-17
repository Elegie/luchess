package io.elegie.luchess.core.indexing.workflow.helpers;

import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.indexing.adapters.game.GameSourceDataUnit;

/**
 * A source data unit wrapping a single game.
 */
public class SingleGameSourceDataUnit extends GameSourceDataUnit {

    private boolean beforeProcessingCalled = false;
    private boolean afterProcessingCalled = false;

    /**
     * @param game
     *            The game to be wrapped.
     */
    public SingleGameSourceDataUnit(Game game) {
        super(game);
    }

    @Override
    public void beforeProcessing() {
        super.beforeProcessing();
        beforeProcessingCalled = true;
    }

    @Override
    public void afterProcessing(boolean success) {
        super.afterProcessing(success);
        afterProcessingCalled = true;
        if (!beforeProcessingCalled) {
            String message = "afterProcessing called, but beforeProcessing was not called.";
            throw new AssertionError(message);
        }
    }

    /**
     * @return True when all methods of the lifecycle have been properly called.
     */
    public boolean lifeCycleComplete() {
        return beforeProcessingCalled && afterProcessingCalled;
    }

}
