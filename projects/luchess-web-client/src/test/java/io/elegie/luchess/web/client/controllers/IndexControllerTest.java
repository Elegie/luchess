package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;
import io.elegie.luchess.web.framework.payload.Result;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class IndexControllerTest extends ConfigurationSetupTest {

    @Test
    public void testIndexNoBuildError() {
        Result result = new IndexController().index();
        assertNotNull(result.getModel().get(Models.MONITOR.getValue()));
        assertNull(result.getModel().get(Models.ERROR.getValue()));
        assertEquals(ServicesFactoryTestHelper.TOTAL_GAMES, result.getModel().get(Models.NUM_GAMES.getValue()));
    }

    @Test
    public void testIndexWithBuildError() {
        ClientContext.INSTANCE.getMonitor().failed(null);
        Result result = new IndexController().index();
        assertNotNull(result.getModel().get(Models.ERROR.getValue()));
    }

}
