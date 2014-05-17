package io.elegie.luchess.app.lucene4x;

import io.elegie.luchess.app.lucene4x.configuration.AppConfig;
import io.elegie.luchess.app.lucene4x.configuration.AppConfigPropertiesImpl;
import io.elegie.luchess.core.api.ServicesFactory;

import java.util.Properties;

/**
 * A simple implementation of the {@link ServicesFactory}, in which we plug in
 * our own implementations of the build/search services.
 */
public class ServicesFactoryImpl implements ServicesFactory {

    private BuilderServiceImpl builder;
    private ExplorerServiceImpl explorer;
    private IndexInfoServiceImpl info;
    private AppConfig config;
    private boolean initialized = false;

    /**
     * All services are created in the initialization, and kept in memory in the
     * factory instance. Clients must therefore create the factory once, and
     * reuse it across their execution flows.
     */
    @Override
    public void initialize(Properties parameters) {
        if (initialized) {
            String message = "The factory has already been initialized.";
            throw new IllegalStateException(message);
        }

        config = new AppConfigPropertiesImpl(parameters);
        builder = new BuilderServiceImpl(this);
        explorer = new ExplorerServiceImpl(this);
        info = new IndexInfoServiceImpl(this);
        initialized = true;
    }

    @Override
    public BuilderServiceImpl getBuilderService() {
        return builder;
    }

    @Override
    public ExplorerServiceImpl getExplorerService() {
        return explorer;
    }

    @Override
    public IndexInfoServiceImpl getIndexInfoService() {
        return info;
    }

    /**
     * @return The configuration object of the factory. The configuration is
     *         created once when the factory is instantiated, and cannot be
     *         changed afterwards.
     */
    public AppConfig getConfig() {
        return config;
    }

}
