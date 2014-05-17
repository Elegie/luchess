package io.elegie.luchess.web.framework.routing.controllers;

import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Class that is not a controller, used to test utility methods for controllers.
 * The method marked as controller is malformed, as it does not return a result.
 */
@SuppressWarnings("javadoc")
public class MalformedControllerNoResult {

    @Controller
    public void baz() {

    }

}
