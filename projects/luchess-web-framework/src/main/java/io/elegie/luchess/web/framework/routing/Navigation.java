package io.elegie.luchess.web.framework.routing;

import java.util.List;

/**
 * This object holds all navigation paths, derived from available controllers.
 * It can be used directly into templates, so as to build proper URLs to action
 * controllers.
 */
public class Navigation {

    private Router router;

    /**
     * @param router
     *            The router from which one can get the list of all available
     *            controllers.
     */
    public Navigation(Router router) {
        this.router = router;
    }

    /**
     * The method to call in the template, in order to call a given action
     * controller.
     * 
     * @param code
     *            The code for the controller, which must be contained with the
     *            class name of the controller.
     * @return A URL to the controller, to be contextualized by the view.
     */
    public String go(String code) {
        String searchedCode = code.toLowerCase();
        List<Class<?>> controllers = router.getAllControllers();
        for (Class<?> controller : controllers) {
            String controllerName = controller.getSimpleName().toLowerCase();
            if (controllerName.contains(searchedCode)) {
                return router.reverse(controller);
            }
        }
        String message = "Routing code not recognized (%s).";
        message = String.format(message, code);
        throw new IllegalArgumentException(message);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Class<?> controller : router.getAllControllers()) {
            if (!first) {
                builder.append(", ");
            }
            builder.append(controller.getSimpleName()).append(": ").append(router.reverse(controller));
            first = false;
        }
        return this.getClass().getSimpleName() + " {" + builder.toString() + "}";
    }
}
