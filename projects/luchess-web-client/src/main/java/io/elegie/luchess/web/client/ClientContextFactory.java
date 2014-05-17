package io.elegie.luchess.web.client;

import io.elegie.luchess.web.framework.ApplicationContext;
import io.elegie.luchess.web.framework.ApplicationContextFactory;
import io.elegie.luchess.web.framework.exceptions.ApplicationException;

import java.util.Properties;

/**
 * Used to create an application context. Note that we reuse the same context
 * throughout the lifetime of our application, so the factory always returns the
 * same object, simply changing its properties if called repeatedly (which
 * should never happen, as per the contract of the framework).
 */
public class ClientContextFactory implements ApplicationContextFactory {

    @Override
    public ApplicationContext create(Properties configuration) throws ApplicationException {
        ClientContext context = ClientContext.INSTANCE;
        try {
            context.configure(configuration);
        } catch (ClientContextException e) {
            String message = "Cannot configure the context.";
            throw new ApplicationException(message, e);
        }
        return context;
    }

}
