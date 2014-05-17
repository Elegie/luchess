package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * This controller is used to send back the monitor, with its updated values. It
 * may be called on a regular basis, using background HTTP requests, so as to
 * report the progress of a running build session.
 */
public class ProgressController {

    /**
     * @return The updated monitor.
     */
    @Controller
    public Result progress() {
        Model model = Models.createEmptyModel();
        model.put(Models.MONITOR.getValue(), ClientContext.INSTANCE.getMonitor());
        return new Result(Views.PROGRESS.getName(), model);
    }

}
