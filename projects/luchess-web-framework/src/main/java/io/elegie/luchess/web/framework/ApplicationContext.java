package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.routing.Router;

/**
 * The application context is created once, at the start of the application.
 * Clients should provide their own implementation, selecting which router and
 * presenter should be used during the processing of web requests.
 * 
 * @see ApplicationContextFactory
 */
public interface ApplicationContext {

    /**
     * @return The router to be used when dispatching the web request.
     */
    Router getRouter();

    /**
     * @return The presenter to be used when rendering the result.
     */
    Presenter getPresenter();

}
