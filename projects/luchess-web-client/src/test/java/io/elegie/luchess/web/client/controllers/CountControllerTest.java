package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertEquals;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CountControllerTest extends ConfigurationSetupTest {

    @Test
    public void testCountSuccess() {
        assertEquals(ServicesFactoryTestHelper.TOTAL_GAMES, new CountController().count().getModel().get(Models.NUM_GAMES.getValue()));
    }

    @Test
    public void testCountFailure() {
        ((ServicesFactoryTestHelper) ClientContext.INSTANCE.getServicesFactory()).setThrowExceptionOnCount(true);
        assertEquals(0, new CountController().count().getModel().get(Models.NUM_GAMES.getValue()));
    }

}
