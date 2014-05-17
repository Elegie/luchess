package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.helpers.GameHelper;
import io.elegie.luchess.web.client.models.GameInfo;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for controllers which return a particular game.
 */
public abstract class AbstractGetGameController {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGetGameController.class);

    /**
     * @return The error to be displayed when the game cannot be found.
     */
    protected abstract Models errorOnInvalidGame();

    /**
     * @param game
     *            The found game.
     * @return The proper result for the subclass controller.
     */
    protected abstract Result send(Game game);

    /**
     * Template method for the game retrieval. If the game is not found, then
     * the template method returns the search controller output with an error,
     * otherwise it delegates the rendering of the game to subclasses.
     * 
     * @param gameId
     *            The id of the game.
     * @return The game associated with the proper view, as determined by the
     *         subclass.
     */
    @Controller
    public final Result retrieve(GameInfo gameId) {
        try {
            String id = gameId.getId();
            Game game = GameHelper.getGame(id);
            if (game != null) {
                return send(game);
            }
        } catch (QueryException e) {
            String message = "Cannot execute query.";
            LOG.warn(message, e);
        }
        Result result = new SearchController().search();
        result.getModel().put(Models.ERROR.getValue(), errorOnInvalidGame().getValue());
        return result;
    }

}
