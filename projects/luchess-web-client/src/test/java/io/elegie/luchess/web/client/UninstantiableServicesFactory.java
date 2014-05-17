package io.elegie.luchess.web.client;

import io.elegie.luchess.core.api.BuilderService;
import io.elegie.luchess.core.api.ExplorerService;
import io.elegie.luchess.core.api.IndexInfoService;
import io.elegie.luchess.core.api.ServicesFactory;

import java.util.Properties;

/**
 * A factory which cannot be instantiated. Used to test the client context
 * creation.
 */
public class UninstantiableServicesFactory implements ServicesFactory {

    private UninstantiableServicesFactory() {
    }

    @Override
    public void initialize(Properties parameters) {
    }

    @Override
    public BuilderService getBuilderService() {
        return null;
    }

    @Override
    public ExplorerService getExplorerService() {
        return null;
    }

    @Override
    public IndexInfoService getIndexInfoService() {
        return null;
    }

}
