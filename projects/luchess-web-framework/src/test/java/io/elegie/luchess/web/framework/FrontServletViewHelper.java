package io.elegie.luchess.web.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.View;

/**
 * A View implementation which, when displayed, asserts that the model contains
 * the key {@value #MODEL_KEY} with the value {@value #MODEL_VALUE}.
 */
@SuppressWarnings("javadoc")
public class FrontServletViewHelper implements View {

    public static final String MODEL_KEY = "key";
    public static final String MODEL_VALUE = "value";

    @Override
    public void display(WebContext context, Model model) {
        if (model != null) {
            Object value = model.get(MODEL_KEY);
            if (value != null) {
                assertEquals(MODEL_VALUE, value.toString());
                return;
            }
        }
        String message = "Model should contain value %s for key %, and does not (%).";
        message = String.format(message, MODEL_KEY, MODEL_VALUE, model);
        fail(message);
    }

}
