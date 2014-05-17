package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

/**
 * Returns the fully qualified name of the {@link FrontServletViewHelper} view,
 * with the model filled in accordingly.
 */
public class FrontServletControllerHelper {

    @SuppressWarnings("javadoc")
    @Controller
    public Result process() {
        Model model = new Model();
        model.put(FrontServletViewHelper.MODEL_KEY, FrontServletViewHelper.MODEL_VALUE);
        return new Result(FrontServletViewHelper.class.getCanonicalName(), model);
    }
}
