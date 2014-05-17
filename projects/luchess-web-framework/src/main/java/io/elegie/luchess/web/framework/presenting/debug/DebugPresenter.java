package io.elegie.luchess.web.framework.presenting.debug;

import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.View;

/**
 * This presenter always returns the same view, a debugging view that lets us
 * inspect parameters.
 */
public class DebugPresenter implements Presenter {

    @Override
    public View resolve(String viewName) {
        return new DebugView(viewName);
    }

}
