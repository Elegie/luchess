package io.elegie.luchess.web.launcher.services.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.web.launcher.services.api.ServerException;
import io.elegie.luchess.web.launcher.services.api.ServerService;
import io.elegie.luchess.web.launcher.services.api.WebApp;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class JettyServerServiceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullWebApp() throws ServerException {
        new JettyServerService().start(null);
    }

    @Test
    public void testStartStop() throws ServerException {
        ServerService serverService = new JettyServerService();
        assertFalse(serverService.isStarted());
        assertTrue(serverService.isStopped());
        serverService.start(new WebApp("", ""));
        assertTrue(serverService.isStarted());
        assertFalse(serverService.isStopped());
        serverService.stop();
        assertFalse(serverService.isStarted());
        assertTrue(serverService.isStopped());
    }

}
