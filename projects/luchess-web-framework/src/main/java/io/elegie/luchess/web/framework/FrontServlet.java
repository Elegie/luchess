package io.elegie.luchess.web.framework;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.View;
import io.elegie.luchess.web.framework.routing.WebRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This is the main entry point to our MVC-based web framework.
 * </p>
 * 
 * <p>
 * The servlet acts as a Front Controller. It receives all application requests,
 * analyze them, have the proper action controller be created then executed, and
 * have the proper view be created then displayed, with data provided by the
 * controller. Configuration of the servlet is done with an {@link Initializer}.
 * </p>
 * 
 * <p>
 * Consumers of the framework should provide an
 * {@link ApplicationContextFactory}, so as to let the framework create and
 * reuse an {@link ApplicationContext}.
 * </p>
 * 
 * <p>
 * The servlet may be called as a result of any web filter, such as the
 * {@link ResourceDispatcher}.
 * </p>
 */
@SuppressWarnings("serial")
public class FrontServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(FrontServlet.class);

    private ApplicationContext appContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            appContext = Initializer.createContext(config);
            if (appContext.getPresenter() == null || appContext.getRouter() == null) {
                String message = "Presenter and router must not be null.";
                throw new ApplicationException(message);
            }
        } catch (ApplicationException e) {
            LOG.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            dispatch(req, resp);
        } catch (ApplicationException e) {
            LOG.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * There lies the lifecycle of the request.
     * 
     * @param req
     *            The HTTP request, from which one can grab data to locate and
     *            execute the right action controller.
     * @param resp
     *            The HTTP response, to be written to when the view is
     *            displayed.
     * @throws ApplicationException
     *             When the request flow cannot be processed.
     */
    private void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ApplicationException {
        WebContext webContext = new WebContext(req, resp);
        Object controller = appContext.getRouter().resolve(webContext);
        Result result = new WebRequest(webContext).process(controller);
        View view = appContext.getPresenter().resolve(result.getViewName());
        view.display(webContext, result.getModel());
    }

}
