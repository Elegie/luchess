package io.elegie.luchess.app.lucene4x;

/**
 * Base class for all services implementations. Provides an access to the
 * factory, so that any service can call any other service.
 */
public abstract class AbstractService {

    private ServicesFactoryImpl servicesFactory;

    /**
     * @param servicesFactory
     *            The factory to be used so that a service can access other
     *            services.
     */
    public AbstractService(ServicesFactoryImpl servicesFactory) {
        this.servicesFactory = servicesFactory;
    }

    protected ServicesFactoryImpl getServicesFactory() {
        return servicesFactory;
    }

}
