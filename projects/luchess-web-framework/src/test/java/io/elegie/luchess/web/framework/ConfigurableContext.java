package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.routing.Router;

/**
 * A context which has its router and presenter injected at construction.
 */
@SuppressWarnings("javadoc")
public class ConfigurableContext implements ApplicationContext {

    private Router router;
    private Presenter presenter;

    public ConfigurableContext(Router router, Presenter presenter) {
        this.router = router;
        this.presenter = presenter;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

}
