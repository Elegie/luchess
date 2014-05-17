package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.web.client.ConfigurationSetupTest;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ErrorControllerTest extends ConfigurationSetupTest {

    @Test
    public void testError() {
        new ErrorController().error();
    }

}
