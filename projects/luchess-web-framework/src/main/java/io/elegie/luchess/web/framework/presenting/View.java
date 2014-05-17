package io.elegie.luchess.web.framework.presenting;

import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;

/**
 * A view is fed with model data, then can render itself, using the response
 * printer in some way. It is usually built around a template, which generates
 * text content (such as HTML, JSON...).
 */
public interface View {

    /**
     * @param context
     *            From which we get the output stream.
     * @param model
     *            To be fed to the view.
     */
    void display(WebContext context, Model model);

}
