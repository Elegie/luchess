package io.elegie.luchess.web.framework.routing.fqnbased;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.routing.Router;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Router which looks up the name of the controller from its parameters. The
 * name should be associated with a parameter specified by the key:
 * {@value #CONTROLLER_NAME}.
 */
public class FQNBasedRouter implements Router {

    /**
     * Key for the name of the controller, which must be specified in the
     * request parameters: {@value #CONTROLLER_NAME}.
     */
    public static final String CONTROLLER_NAME = "controller";

    private static final Logger LOG = LoggerFactory.getLogger(FQNBasedRouter.class);

    @Override
    public Object resolve(WebContext context) throws ApplicationException {
        String controllerName = context.getRequest().getParameter(CONTROLLER_NAME);
        if (controllerName != null) {
            try {
                return Class.forName(controllerName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                String message = "Cannot create controller from name: %s";
                message = String.format(message, controllerName);
                LOG.error(message, e);
            }
        } else {
            String message = "Controller name must not be null.";
            LOG.error(message);
        }
        return null;
    }

    @Override
    public String reverse(Class<?> controller) {
        String controllerName = "";
        if (controller != null) {
            controllerName = controller.getCanonicalName();
        }
        return '?' + CONTROLLER_NAME + '=' + controllerName;
    }

    @Override
    public List<Class<?>> getAllControllers() {
        return Collections.emptyList();
    }

}
