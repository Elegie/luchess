package io.elegie.luchess.web.framework.routing.controllers;

import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Dummy controller used to test the controller utility methods.
 */
@SuppressWarnings("javadoc")
public class FooController {

    @Controller
    public Result foo() {
        return null;
    }

}
