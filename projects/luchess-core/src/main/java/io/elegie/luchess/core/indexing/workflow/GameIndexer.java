package io.elegie.luchess.core.indexing.workflow;

import io.elegie.luchess.core.domain.entities.Game;

import java.io.IOException;

/**
 * <p>
 * This interface is used by the framework to notify implementations that a new
 * game has been parsed, so that they include it into their index.
 * </p>
 * 
 * <p>
 * It is supposed to be used with an {@link IndexingSession}, so implementations
 * should make sure that their derived classes be thread-safe.
 * </p>
 */
public interface GameIndexer {

    /**
     * @param game
     *            The game game retrieved by the parser.
     * @throws IOException
     *             Implementations may throw an exception when they cannot
     *             process the game.
     */
    void add(Game game) throws IOException;

}
