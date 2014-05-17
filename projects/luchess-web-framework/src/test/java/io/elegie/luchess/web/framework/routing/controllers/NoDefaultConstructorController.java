package io.elegie.luchess.web.framework.routing.controllers;

import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Controller used to make sure that the resolving fails, because the controller
 * has no default constructor.
 */
@SuppressWarnings("javadoc")
public class NoDefaultConstructorController {
    @SuppressWarnings("unused")
    public NoDefaultConstructorController(String foo) {
    }

    @Controller
    public Result foo() {
        return null;
    }
}
