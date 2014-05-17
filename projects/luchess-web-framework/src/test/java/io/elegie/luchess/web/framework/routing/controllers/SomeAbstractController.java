package io.elegie.luchess.web.framework.routing.controllers;

import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Abstract controllers should not be retained in the final controller list.
 */
@SuppressWarnings("javadoc")
public abstract class SomeAbstractController {

    @Controller
    public Result foo() {
        return null;
    }
}
