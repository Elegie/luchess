package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;
import io.elegie.luchess.web.client.models.BuildInfo;
import io.elegie.luchess.web.framework.payload.Result;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class BuildControllerTest extends ConfigurationSetupTest {

    @Test(expected = IllegalStateException.class)
    public void testBuildAlreadyRunning() {
        ClientContext.INSTANCE.getMonitor().started();
        new BuildController().build(new BuildInfo());
    }

    @Test
    public void testBuildSuccess() {
        new BuildController().build(new BuildInfo());
    }

    @Test
    public void testBuildFailure() {
        ((ServicesFactoryTestHelper) ClientContext.INSTANCE.getServicesFactory()).setThrowExceptionOnBuild(true);
        Result result = new BuildController().build(new BuildInfo());
        assertNotNull(result.getModel().get(Models.ERROR.getValue()));
    }
}
