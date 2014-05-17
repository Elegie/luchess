package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;

import java.util.Properties;

/**
 * Implementations should provide a factory, so that the application context be
 * properly created by the framework. This context shall be created only once,
 * and reused throughout the application lifetime.
 */
public interface ApplicationContextFactory {

    /**
     * @param configuration
     *            All servlet parameters defined in the web.xml file. Clients
     *            may specify additional configuration files in the web.xml,
     *            retrieving their paths thanks to this method.
     * @return An initialized context, to be used throughout the application.
     * @throws ApplicationException
     *             When the application context cannot be created
     */
    ApplicationContext create(Properties configuration) throws ApplicationException;

}
