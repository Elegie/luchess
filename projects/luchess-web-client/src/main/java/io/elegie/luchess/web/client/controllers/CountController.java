package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.api.IndexInfoService;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This controller is used to get the total of games in the index.
 */
public class CountController {

    private static final Logger LOG = LoggerFactory.getLogger(CountController.class);

    /**
     * @return The total of games in the index, using a JSON view.
     */
    @Controller
    public Result count() {
        Model model = Models.createEmptyModel();
        addGamesCount(model);
        return new Result(Views.COUNT.getName(), model);
    }

    private void addGamesCount(Model model) {
        IndexInfoService indexInfoService = ClientContext.INSTANCE.getServicesFactory().getIndexInfoService();
        int gamesCount = 0;
        try {
            gamesCount = indexInfoService.getGamesCount();
        } catch (QueryException e) {
            String message = "Cannot find number of games. The index may not already exist.";
            LOG.warn(message, e);
        }
        model.put(Models.NUM_GAMES.getValue(), gamesCount);
    }

}
