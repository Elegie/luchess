package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;

/**
 * <p>
 * The initializer is in charge of loading and initializing the client
 * application context, using a context factory, so that the application be
 * properly configured.
 * </p>
 * 
 * <p>
 * Implementations should provide their own context factory, specifying the
 * class name of the factory in the web.xml file, using the following key
 * {@value #CONTEXT_FACTORY_CLASS}.
 * </p>
 */
public final class Initializer {

    /**
     * The key to be used in the web.xml, in order to specify an
     * {@link ApplicationContextFactory} client class:
     * {@value #CONTEXT_FACTORY_CLASS}.
     */
    public static final String CONTEXT_FACTORY_CLASS = "framework.context.factory.class";

    private Initializer() {

    }

    /**
     * @param config
     *            The Servlet configuration, from which one can access the
     *            properties located in the web.xml.
     * @return An initialized application context.
     * @throws ApplicationException
     */
    public static ApplicationContext createContext(ServletConfig config) throws ApplicationException {
        try {
            String className = config.getInitParameter(CONTEXT_FACTORY_CLASS);
            if (className == null) {
                String message = "Missing init parameter for factory class: %s.";
                message = String.format(message, CONTEXT_FACTORY_CLASS);
                throw new ClassNotFoundException(message);
            }
            ApplicationContextFactory factory = (ApplicationContextFactory) Class.forName(className).newInstance();
            return factory.create(createConfiguration(config));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ApplicationException e) {
            String message = "Cannot create the application context.";
            throw new ApplicationException(message, e);
        }
    }

    /**
     * @param config
     *            The servlet configuration.
     * @return A properties file made of the servlet configuration parameters.
     */
    private static Properties createConfiguration(ServletConfig config) {
        Properties properties = new Properties();
        @SuppressWarnings("unchecked")
        Enumeration<String> paramNames = config.getInitParameterNames();
        if (paramNames != null) {
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = config.getInitParameter(paramName);
                if (paramValue == null) {
                    paramValue = "";
                }
                properties.put(paramName, paramValue);
            }
        }
        return properties;
    }

}
