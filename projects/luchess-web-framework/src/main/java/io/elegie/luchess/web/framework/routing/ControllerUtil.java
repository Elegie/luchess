package io.elegie.luchess.web.framework.routing;

import io.elegie.luchess.web.framework.payload.Result;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides utility methods to work with controllers. These methods
 * may be relocated elsewhere, once we get enough feedback about the use of the
 * framework.
 */
public final class ControllerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerUtil.class);

    private ControllerUtil() {

    }

    // --- Discover controllers inside a package ------------------------------

    private static final String CLASS_SUFFIX = ".class";

    /**
     * Discovers all controllers from a given package, searching recursively.
     * 
     * @param scannedPackage
     *            The root package to be searched.
     * @return A list of all controllers contained within the package (or its
     *         children).
     */
    public static List<Class<?>> findControllersByPackage(String scannedPackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String scannedPath = scannedPackage.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scannedPath);
        } catch (IOException e) {
            String message = "Cannot find resources from \"%s\".";
            message = String.format(message, scannedPackage);
            throw new IllegalArgumentException(message, e);
        }
        List<Class<?>> classes = new LinkedList<>();
        while (resources.hasMoreElements()) {
            String path = resources.nextElement().getFile();
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                String message = "Cannot decode %s. Trying without decoding.";
                message = String.format(message, path);
                LOG.warn(message, e);
            }
            File file = new File(path);
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new LinkedList<>();
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                classes.addAll(find(nestedFile, scannedPackage));
            }
        } else if (file.getName().endsWith(CLASS_SUFFIX) && !file.getName().contains("$")) {
            int endIndex = file.getName().length() - CLASS_SUFFIX.length();
            String className = file.getName().substring(0, endIndex);
            try {
                final String resource = scannedPackage + '.' + className;
                Class<?> controllerClass = Class.forName(resource);
                if (!Modifier.isAbstract(controllerClass.getModifiers()) && findControllerMethod(controllerClass) != null) {
                    classes.add(controllerClass);
                }
            } catch (ClassNotFoundException | ControllerException e) {
                String message = "Ignoring class %s as potential controller.";
                message = String.format(message, className);
                LOG.error(message, e);
            }
        } else {
            String message = "Ignoring file (%s), not a directory and not a class.";
            message = String.format(message, file);
            LOG.warn(message);
        }
        return classes;
    }

    // --- Find and check controller method -----------------------------------

    /**
     * Given an object, find the controller method (inspecting all of its public
     * methods), or return null if no such method is found.
     * 
     * @param controller
     *            The class of the object to assert as a controller.
     * @return The controller method, or null if no such method.
     * @throws ControllerException
     *             When a controller method has been specified on the class, but
     *             appears to be malformed.
     */
    public static Method findControllerMethod(Class<?> controller) throws ControllerException {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            Controller annotation = method.getAnnotation(Controller.class);
            if (annotation != null) {
                checkReturnType(method);
                checkParameters(method);
                return method;
            }
        }
        return null;
    }

    private static void checkReturnType(Method method) throws ControllerException {
        if (!Result.class.isAssignableFrom(method.getReturnType())) {
            String message = "Controller method %s should return a Result.";
            message = String.format(message, method.getName());
            throw new ControllerException(message);
        }
    }

    private static void checkParameters(Method method) throws ControllerException {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length > 1) {
            String message = "Controller method %s may not have more than one parameters.";
            message = String.format(message, method.getName());
            throw new ControllerException(message);
        }
    }

}
