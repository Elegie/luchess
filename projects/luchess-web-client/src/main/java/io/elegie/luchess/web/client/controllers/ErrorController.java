package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Controller used to display a clean error to the user. The details of the
 * error should in the log, so we simply send back a generic error page.
 */
@SuppressWarnings("javadoc")
public class ErrorController {

    @Controller
    public Result error() {
        return new Result(Views.ERROR.getName(), Models.createModelWithNavigation());
    }

}
