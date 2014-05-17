package io.elegie.luchess.web.framework.routing;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.payload.WebContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The invoker is in charge of invoking the controller method of the controller,
 * converting the parameters contained in the web request to the format expected
 * by the method.
 */
public class WebRequest {

    private static final Logger LOG = LoggerFactory.getLogger(WebRequest.class);
    private static final String SETTER_PREFIX = "set";
    private static final List<Class<?>> OBJECTS = new ArrayList<>();
    private static final List<Class<?>> LITERALS = new ArrayList<>();
    private static final Map<Class<?>, Class<?>> COLLECTIONS = new HashMap<>();

    static {
        OBJECTS.add(Integer.class);
        OBJECTS.add(Long.class);
        OBJECTS.add(Float.class);
        OBJECTS.add(Double.class);
        OBJECTS.add(String.class);

        LITERALS.add(int.class);
        LITERALS.add(long.class);
        LITERALS.add(float.class);
        LITERALS.add(double.class);

        COLLECTIONS.put(List.class, ArrayList.class);
        COLLECTIONS.put(Set.class, HashSet.class);
    }

    private WebContext webContext;

    /**
     * Create the web request.
     * 
     * @param webContext
     *            From which the raw parameters should be retrieved.
     */
    public WebRequest(WebContext webContext) {
        this.webContext = webContext;
        if (webContext == null) {
            String message = "webContext must not be null.";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param controller
     *            The controller used to process the request. Must contain a
     *            controller method.
     * @return An invocation Result, containing the view name and the model, as
     *         determined by the controller method.
     * @throws ApplicationException
     *             When the controller cannot be processed.
     */
    public Result process(Object controller) throws ApplicationException {
        try {
            Method method = ControllerUtil.findControllerMethod(controller.getClass());
            if (method == null) {
                String message = "No controller method found for class %s";
                message = String.format(message, controller.getClass());
                throw new ControllerException(message);
            }
            Object[] arguments = null;
            int arity = method.getParameterTypes().length;
            if (arity == 1) {
                arguments = new Object[arity];
                arguments[0] = createParameter(webContext, method.getParameterTypes()[0]);
            }
            return (Result) method.invoke(controller, arguments);
        } catch (IllegalAccessException | IllegalArgumentException | ControllerException e) {
            String message = "Controller %s cannot be invoked (%s).";
            message = String.format(message, controller, e.getMessage());
            throw new ApplicationException(message, e);
        } catch (InvocationTargetException e) {
            String message = "The execution of %s failed (%s).";
            message = String.format(message, controller.getClass().getSimpleName(), e.getCause().getMessage());
            throw new ApplicationException(message, e);
        }
    }

    @SuppressWarnings("unchecked")
    private Object createParameter(WebContext context, Class<?> argumentClass) throws ApplicationException {
        Object argument = null;
        try {
            argument = argumentClass.newInstance();
            Map<String, String[]> parameters = context.getRequest().getParameterMap();
            for (Entry<String, String[]> entry : parameters.entrySet()) {
                tryAssign(entry.getKey(), entry.getValue(), argument);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "Cannot create argument.";
            throw new ApplicationException(message, e);
        }
        return argument;
    }

    private void tryAssign(String parameterName, String[] parameterValue, Object argument) {
        Method[] methods = argument.getClass().getMethods();
        for (Method method : methods) {
            if ((SETTER_PREFIX + parameterName).equalsIgnoreCase(method.getName())) {
                tryAssignMethod(method, parameterValue, argument);
            }
        }
    }

    private void tryAssignMethod(Method method, String[] parameterValue, Object argument) {
        Type[] methodParameterTypes = method.getGenericParameterTypes();
        if (methodParameterTypes.length == 1) {
            Type methodParameterType = methodParameterTypes[0];
            if (methodParameterType instanceof ParameterizedType) {
                Class<?> parameterClass = (Class<?>) ((ParameterizedType) methodParameterType).getRawType();
                Class<?> genericClass = (Class<?>) ((ParameterizedType) methodParameterType).getActualTypeArguments()[0];
                tryAssignCollection(parameterClass, genericClass, argument, method, parameterValue);
            } else {
                Class<?> parameterClass = (Class<?>) methodParameterType;
                String singleValue = parameterValue[0];
                if (!singleValue.isEmpty()) {
                    tryAssignObject(parameterClass, argument, method, singleValue);
                    tryAssignLiteral(parameterClass, argument, method, singleValue);
                    tryAssignBoolean(parameterClass, argument, method, singleValue);
                }
            }
        }
    }

    private void tryAssignObject(Class<?> parameterClass, Object argument, Method method, String singleValue) {
        for (Class<?> object : OBJECTS) {
            if (parameterClass.isAssignableFrom(object)) {
                try {
                    method.invoke(argument, object.getConstructor(String.class).newInstance(singleValue));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | InstantiationException | NoSuchMethodException | SecurityException e) {
                    handleException(e, parameterClass.getSimpleName(), singleValue, method);
                }
                break;
            }
        }
    }

    private void tryAssignLiteral(Class<?> parameterClass, Object argument, Method method, String singleValue) {
        int index = 0;
        for (Class<?> literal : LITERALS) {
            if (parameterClass.equals(literal)) {
                try {
                    method.invoke(argument, OBJECTS.get(index).getConstructor(String.class).newInstance(singleValue));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | InstantiationException | NoSuchMethodException | SecurityException e) {
                    handleException(e, parameterClass.getSimpleName(), singleValue, method);
                }
                break;
            }
            index++;
        }
    }

    private void tryAssignBoolean(Class<?> parameterClass, Object argument, Method method, String singleValue) {
        if (parameterClass.equals(boolean.class) || parameterClass.isAssignableFrom(Boolean.class)) {
            try {
                if ("0".equals(singleValue) || "false".equals(singleValue)) {
                    method.invoke(argument, Boolean.FALSE);
                } else if ("1".equals(singleValue) || "true".equals(singleValue)) {
                    method.invoke(argument, Boolean.TRUE);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                handleException(e, "boolean", singleValue, method);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void tryAssignCollection(Class<?> parameterClass, Class<?> genericClass, Object argument, Method method,
            String[] parameterValue) {
        for (Class<?> collection : COLLECTIONS.keySet()) {
            if (parameterClass.isAssignableFrom(collection)) {
                try {
                    @SuppressWarnings("rawtypes")
                    Collection target = (Collection) COLLECTIONS.get(collection).newInstance();
                    for (String value : parameterValue) {
                        target.add(genericClass.getConstructor(String.class).newInstance(value));
                    }
                    method.invoke(argument, target);
                    break;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    handleException(e, parameterClass + "<" + genericClass + ">", parameterValue, method);
                }
            }
        }
    }

    private void handleException(Exception e, String type, Object value, Method method) {
        String message = "Cannot assign %s \"%s\" to method %s";
        message = String.format(message, type, value, method);
        LOG.error(message, e);
    }
}
