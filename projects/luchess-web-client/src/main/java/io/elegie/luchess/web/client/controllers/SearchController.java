package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.domain.entities.Board;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * This is the main controller for the search screen.
 */
public class SearchController {

    /**
     * @return An initialized searched screen, with no search criteria
     *         specified.
     */
    @Controller
    public Result search() {
        Model model = Models.createModelWithNavigation();
        model.put(Models.BOARD.getValue(), new Board());
        return new Result(Views.SEARCH.getName(), model);
    }

}
