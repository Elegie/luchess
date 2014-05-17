package io.elegie.luchess.app.lucene_4x;

import io.elegie.luchess.app.lucene_4x.helpers.ServicesFactoryHelper;
import io.elegie.luchess.core.api.AbstractServicesIntegrationTest;
import io.elegie.luchess.core.api.ServicesFactory;

/**
 * Our implementation of the integration testing suite provide by the core. We
 * simply return a properly initialized factory when asked for it.
 */
public class ServicesIntegrationTest extends AbstractServicesIntegrationTest {

    @Override
    public ServicesFactory createServicesFactory(boolean createMode) {
        return ServicesFactoryHelper.createInMemoryIndexFactory(createMode, MOVETEXT_LENGTH);
    }

    @Override
    protected boolean testSimilarPlayerNameMatch() {
        return true;
    }

}
