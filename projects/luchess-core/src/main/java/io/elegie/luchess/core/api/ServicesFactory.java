package io.elegie.luchess.core.api;

import java.util.Properties;

/**
 * The factory is the entry point used to obtain references to all services.
 * Implementations are free to provide any indexing mechanism they like, such as
 * a search technology, a relational database, as long as they respect the API
 * defined for each service.
 */
public interface ServicesFactory {

    /**
     * Any client creating a factory should call the initialize method right
     * after the factory has been created.
     * 
     * @param parameters
     *            List of parameters to be transmitted to the underlying
     *            implementation. It is up to the implementation to make sure
     *            that all necessary configurations are made available to
     *            services.
     */
    void initialize(Properties parameters);

    /**
     * @return The Builder Service.
     */
    BuilderService getBuilderService();

    /**
     * @return The Explorer Service.
     */
    ExplorerService getExplorerService();

    /**
     * @return The Info Service.
     */
    IndexInfoService getIndexInfoService();

}
