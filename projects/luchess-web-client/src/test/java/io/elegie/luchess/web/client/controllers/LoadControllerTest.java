package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.framework.payload.Model;

@SuppressWarnings("javadoc")
public class LoadControllerTest extends AbstractGetGameControllerTest {

    @Override
    protected AbstractGetGameController getController() {
        return new LoadController();
    }

    @Override
    protected void validateSuccess(Model model) {
        assertNotNull(model.get(Models.SEARCH.getValue()));
    }

}
