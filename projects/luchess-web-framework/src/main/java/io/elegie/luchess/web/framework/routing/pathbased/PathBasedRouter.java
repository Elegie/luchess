package io.elegie.luchess.web.framework.routing.pathbased;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.routing.ControllerException;
import io.elegie.luchess.web.framework.routing.ControllerUtil;
import io.elegie.luchess.web.framework.routing.Router;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This router will automatically match a controller name from the path info,
 * capitalizing at each path part, and appending a controller suffix. For
 * instance, a path like "/books/list" will be translated into
 * "BooksListController", with a suffix being equal to "Controller".
 */
public class PathBasedRouter implements Router {

    private static final String DEFAULT_SUFFIX = "Controller";

    private final List<Class<?>> controllers;
    private final String controllersRootPackage;
    private final String defaultControllerName;

    /**
     * @param controllersRootPackage
     *            The package which contains the controllers, possibly
     *            recursively.
     * @param defaultControllerName
     *            The simple name of the controller to be used for the root URL.
     */
    public PathBasedRouter(String controllersRootPackage, String defaultControllerName) {
        this.controllers = ControllerUtil.findControllersByPackage(controllersRootPackage);
        this.controllersRootPackage = controllersRootPackage + ".";
        this.defaultControllerName = defaultControllerName;
    }

    @Override
    public final Object resolve(WebContext context) throws ApplicationException {
        HttpServletRequest request = context.getRequest();
        String pathInfo = request.getPathInfo();
        String controllerName = defaultControllerName;
        if (pathInfo != null && !pathInfo.isEmpty() && !"/".equals(pathInfo)) {
            controllerName = readControllerName(pathInfo);
        }
        String fullyQualifiedName = controllersRootPackage + controllerName;
        try {
            Class<?> controllerClass = Class.forName(fullyQualifiedName);
            if (!controllers.contains(controllerClass)) {
                String message = "Cannot resolve controller %s.";
                message = String.format(message, fullyQualifiedName);
                throw new ControllerException(message);
            }
            return controllerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ControllerException e) {
            String message = "Cannot find or create controller \"%s\".";
            message = String.format(message, fullyQualifiedName);
            throw new ApplicationException(message, e);
        }
    }

    @Override
    public String reverse(Class<?> controller) {
        String controllerName = controller.getSimpleName();
        if (!controllerName.endsWith(getSuffix())) {
            String message = "Invalid controller object.";
            throw new IllegalArgumentException(message);
        }
        return controllerName.replaceAll("[A-Z]", "/$0").replaceAll(getSuffix() + "$", "").toLowerCase();
    }

    @Override
    public List<Class<?>> getAllControllers() {
        return controllers;
    }

    /**
     * @return The suffix of the controller's name.
     */
    public String getSuffix() {
        return DEFAULT_SUFFIX;
    }

    private String readControllerName(String pathInfo) {
        StringBuilder controllerName = new StringBuilder();
        String[] parts = pathInfo.split("/");
        for (String part : parts) {
            if (part.length() > 0) {
                String firstLetter = part.substring(0, 1).toUpperCase();
                String remainder = part.substring(1);
                controllerName.append(firstLetter).append(remainder);
            }
        }
        controllerName.append(getSuffix());
        return controllerName.toString();
    }

}
