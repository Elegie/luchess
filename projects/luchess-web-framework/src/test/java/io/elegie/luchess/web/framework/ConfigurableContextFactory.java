package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.routing.Router;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory lets us create a context, injecting a router and a presenter
 * specified in the configuration properties.
 */
public class ConfigurableContextFactory implements ApplicationContextFactory {

    /**
     * Key for the router FQN: {@value #ROUTER_KEY}
     */
    public static final String ROUTER_KEY = "router";

    /**
     * Key for the presenter FQN: {@value #PRESENTER_KEY}
     */
    public static final String PRESENTER_KEY = "presenter";

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableContextFactory.class);

    @Override
    public ApplicationContext create(Properties configuration) throws ApplicationException {
        Router router = null;
        Presenter presenter = null;
        try {
            String routerValue = configuration.getProperty(ROUTER_KEY);
            if (routerValue != null && !routerValue.isEmpty()) {
                router = (Router) Class.forName(routerValue).newInstance();
            }
            String presenterValue = configuration.getProperty(PRESENTER_KEY);
            if (presenterValue != null && !presenterValue.isEmpty()) {
                presenter = (Presenter) Class.forName(presenterValue).newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOG.error("Cannot create context", e);
        }
        ConfigurableContext context = new ConfigurableContext(router, presenter);
        return context;
    }

}
