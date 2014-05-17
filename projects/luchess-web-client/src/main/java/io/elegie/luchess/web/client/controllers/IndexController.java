package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.client.models.BuildMonitor;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * <p>
 * The default controller is used when no path is available, typically when the
 * user navigates to the root URL. In this case, we simply render the home page.
 * </p>
 * 
 * <p>
 * A monitor is also included in the view, in order to display the progress of
 * any running build session.
 * </p>
 */
public class IndexController {

    /**
     * @return The home page.
     */
    @Controller
    public Result index() {
        String viewName = Views.INDEX.getName();
        Model model = Models.createModelWithNavigation();
        addMonitor(model);
        addGamesCount(model);
        addIndexingPassword(model);
        return new Result(viewName, model);
    }

    private void addMonitor(Model model) {
        BuildMonitor monitor = ClientContext.INSTANCE.getMonitor();
        model.put(Models.MONITOR.getValue(), monitor);
        if (monitor.acknowledgeFailure()) {
            model.put(Models.ERROR.getValue(), Models.MSG_ERROR_BUILD.getValue());
        }
    }

    private void addGamesCount(Model model) {
        CountController countController = new CountController();
        Result result = countController.count();
        String key = Models.NUM_GAMES.getValue();
        model.put(key, result.getModel().get(key));
    }

    private void addIndexingPassword(Model model) {
        model.put(Models.HAS_PASSWORD.getValue(), ClientContext.INSTANCE.hasIndexingPassword());
    }
}
