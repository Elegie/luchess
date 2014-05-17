package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;
import io.elegie.luchess.web.client.controllers.AbstractGetGameController;
import io.elegie.luchess.web.client.models.GameInfo;
import io.elegie.luchess.web.framework.payload.Model;

import org.junit.Test;

@SuppressWarnings("javadoc")
public abstract class AbstractGetGameControllerTest extends ConfigurationSetupTest {

    protected abstract AbstractGetGameController getController();

    protected abstract void validateSuccess(Model model);

    @Test
    public void testRetrievalSuccess() {
        GameInfo gameId = new GameInfo();
        gameId.setId(ServicesFactoryTestHelper.GAME_ID);
        Model model = getController().retrieve(gameId).getModel();
        assertNotNull(model.get(Models.GAME.getValue()));
        assertNull(model.get(Models.ERROR.getValue()));
        validateSuccess(model);
    }

    @Test
    public void testRetrievalFailure() {
        GameInfo gameId = new GameInfo();
        gameId.setId("unknownId");
        Model model = getController().retrieve(gameId).getModel();
        assertNull(model.get(Models.GAME.getValue()));
        assertNotNull(model.get(Models.ERROR.getValue()));
    }

}
