package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ProgressControllerTest extends ConfigurationSetupTest {

    @Test
    public void testProgress() {
        assertNotNull(new ProgressController().progress().getModel().get(Models.MONITOR.getValue()));
    }
}
