package io.elegie.luchess.web.framework.presenting.debug;

import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.View;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * This is a simple HTML-based view, which prints all data available, for
 * debugging purposes.
 */
public class DebugView implements View {

    private String viewName;

    /**
     * @param viewName
     *            The name of the view to be displayed.
     */
    public DebugView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void display(WebContext context, Model model) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("View = ").append(viewName).append("<br>");
            builder.append("Model = ").append(model).append("<br>");
            HttpServletResponse resp = context.getResponse();
            resp.addHeader("Content-Type", "text/html");
            resp.getWriter().append(builder.toString());
        } catch (IOException e) {
            String message = "Cannot print to the http response.";
            throw new IllegalStateException(message, e);
        }
    }

}
