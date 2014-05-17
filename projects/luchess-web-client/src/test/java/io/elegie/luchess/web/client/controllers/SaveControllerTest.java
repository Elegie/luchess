package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerView;

@SuppressWarnings("javadoc")
public class SaveControllerTest extends AbstractGetGameControllerTest {

    @Override
    protected AbstractGetGameController getController() {
        return new SaveController();
    }

    @Override
    protected void validateSuccess(Model model) {
        assertNotNull(model.get(FreemarkerView.ATTACHMENT_KEY));
    }

}
