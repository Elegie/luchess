package io.elegie.luchess.web.framework.routing;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.WebContext;

import java.util.List;

/**
 * <p>
 * Given a particular web context, a router can select and return a controller
 * instance, to be used to process the web request.
 * </p>
 * 
 * <p>
 * It can also perform the opposite operation, i.e. derive a URL from a
 * controller, which can be used to call that controller.
 * </p>
 * 
 * @see io.elegie.luchess.web.framework.routing.Controller
 */
public interface Router {

    /**
     * @param context
     *            The context from which the router can retrieve data to find
     *            out which controller to return.
     * @return The controller instance for the current web context.
     * @throws ApplicationException
     *             When a controller cannot be resolved from the context.
     */
    Object resolve(WebContext context) throws ApplicationException;

    /**
     * @param controller
     *            The object to be transformed into a URL.
     * @return A URL representing a call to a controller.
     */
    String reverse(Class<?> controller);

    /**
     * @return A List of all available controllers.
     */
    List<Class<?>> getAllControllers();

}
