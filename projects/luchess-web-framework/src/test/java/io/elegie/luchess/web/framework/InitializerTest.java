package io.elegie.luchess.web.framework;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.presenting.fqnbased.FQNBasedPresenter;
import io.elegie.luchess.web.framework.routing.fqnbased.FQNBasedRouter;

import java.util.Vector;

import javax.servlet.ServletConfig;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class InitializerTest {

    private ServletConfig config;

    @Before
    public void setUp() {
        config = mock(ServletConfig.class);
    }

    @Test(expected = ApplicationException.class)
    public void testCreateContextMissingClassName() throws ApplicationException {
        Initializer.createContext(config);
    }

    @Test(expected = ApplicationException.class)
    public void testCreateContextWrongClassName() throws ApplicationException {
        when(config.getInitParameter(Initializer.CONTEXT_FACTORY_CLASS)).thenReturn("unknownClassName");
        Initializer.createContext(config);
    }

    @Test
    public void testCreateContextSuccess() throws ApplicationException {
        Vector<String> params = new Vector<>();
        params.add(ConfigurableContextFactory.ROUTER_KEY);
        params.add(ConfigurableContextFactory.PRESENTER_KEY);
        when(config.getInitParameterNames()).thenReturn(params.elements());
        when(config.getInitParameter(Initializer.CONTEXT_FACTORY_CLASS)).thenReturn(ConfigurableContextFactory.class.getCanonicalName());
        when(config.getInitParameter(ConfigurableContextFactory.ROUTER_KEY)).thenReturn(FQNBasedRouter.class.getCanonicalName());
        when(config.getInitParameter(ConfigurableContextFactory.PRESENTER_KEY)).thenReturn(FQNBasedPresenter.class.getCanonicalName());

        ApplicationContext context = Initializer.createContext(config);
        assertTrue(context.getRouter() instanceof FQNBasedRouter);
        assertTrue(context.getPresenter() instanceof FQNBasedPresenter);
    }
}
