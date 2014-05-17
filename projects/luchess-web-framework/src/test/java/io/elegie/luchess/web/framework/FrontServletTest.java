package io.elegie.luchess.web.framework;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.presenting.fqnbased.FQNBasedPresenter;
import io.elegie.luchess.web.framework.routing.fqnbased.FQNBasedRouter;

import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class FrontServletTest {

    private static final String FACTORY_CLASS_NAME = ConfigurableContextFactory.class.getCanonicalName();
    private static final String ROUTER_CLASS_NAME = FQNBasedRouter.class.getCanonicalName();
    private static final String PRESENTER_CLASS_NAME = FQNBasedPresenter.class.getCanonicalName();

    private FrontServlet servlet;
    private ServletConfig config;

    @Before
    public void setUp() {
        servlet = new FrontServlet();
        config = mock(ServletConfig.class);
        Vector<String> params = new Vector<>();
        params.add(ConfigurableContextFactory.ROUTER_KEY);
        params.add(ConfigurableContextFactory.PRESENTER_KEY);
        when(config.getInitParameterNames()).thenReturn(params.elements());
        when(config.getInitParameter(Initializer.CONTEXT_FACTORY_CLASS)).thenReturn(FACTORY_CLASS_NAME);
    }

    /**
     * A full test, using FQN-based router and presenter, with a dedicated
     * controller and view. The assertion of the workflow being correctly
     * processed is done by the view.
     * 
     * @throws ServletException
     */
    @Test
    public void testWorkflow() throws ServletException {
        when(config.getInitParameter(ConfigurableContextFactory.ROUTER_KEY)).thenReturn(ROUTER_CLASS_NAME);
        when(config.getInitParameter(ConfigurableContextFactory.PRESENTER_KEY)).thenReturn(PRESENTER_CLASS_NAME);
        servlet.init(config);

        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        String controllerName = FrontServletControllerHelper.class.getCanonicalName();
        when(req.getParameter(FQNBasedRouter.CONTROLLER_NAME)).thenReturn(controllerName);
        servlet.doGet(req, resp);
    }

    @Test(expected = ServletException.class)
    public void testNullRouter() throws ServletException {
        when(config.getInitParameter(ConfigurableContextFactory.PRESENTER_KEY)).thenReturn(PRESENTER_CLASS_NAME);
        servlet.init(config);
    }

    @Test(expected = ServletException.class)
    public void testNullPresenter() throws ServletException {
        when(config.getInitParameter(ConfigurableContextFactory.ROUTER_KEY)).thenReturn(ROUTER_CLASS_NAME);
        servlet.init(config);
    }
}
