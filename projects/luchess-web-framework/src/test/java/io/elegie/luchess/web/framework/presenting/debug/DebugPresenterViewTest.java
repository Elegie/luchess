package io.elegie.luchess.web.framework.presenting.debug;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * The debug presenter/view are used to print the view name and the model to the
 * screen, for debugging purposes. We simply check that contents are properly
 * inserted in the response.
 */
@SuppressWarnings("javadoc")
public class DebugPresenterViewTest {

    private static final String VIEW_NAME = "foo";
    private static final String MODEL_KEY = "bar";
    private static final String MODEL_VALUE = "baz";

    @Test
    public void testContentFound() throws IOException {
        Presenter presenter = new DebugPresenter();
        View view = presenter.resolve(VIEW_NAME);
        Model model = new Model();
        model.put(MODEL_KEY, MODEL_VALUE);

        StringWriter writer = new StringWriter();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        WebContext context = new WebContext(req, resp);

        view.display(context, model);
        String content = writer.toString();
        assertTrue(content.contains(VIEW_NAME));
        assertTrue(content.contains(MODEL_KEY));
        assertTrue(content.contains(MODEL_VALUE));
    }
}
