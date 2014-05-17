package io.elegie.luchess.web.framework.routing.controllers;

import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Class that is not a controller, used to test utility methods for controllers.
 * The method marked as controller is malformed, as it has too many arguments.
 */
@SuppressWarnings("javadoc")
public class MalformedControllerTooManyArgs {

    @SuppressWarnings("unused")
    @Controller
    public Result baz(Object arg1, Object arg2) {
        return null;
    }

}
