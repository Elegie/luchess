package io.elegie.luchess.web.framework.routing.fqnbased;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.routing.controllers.FooController;
import io.elegie.luchess.web.framework.routing.controllers.NoDefaultConstructorController;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test that a FQN-based router returns the right controller from a query string
 * argument, and can reverse a controller into query string.
 */
@SuppressWarnings("javadoc")
public class FQNBasedRouterTest {

    private FQNBasedRouter router;
    private WebContext context;

    @Before
    public void setUp() {
        router = new FQNBasedRouter();
        context = new WebContext(mock(HttpServletRequest.class), null);
    }

    @Test
    public void testRouterResolving() throws ApplicationException {
        String className = FooController.class.getCanonicalName();
        when(context.getRequest().getParameter(FQNBasedRouter.CONTROLLER_NAME)).thenReturn(className);
        assertTrue(router.resolve(context) instanceof FooController);
    }

    @Test
    public void testRouterReversing() {
        assertTrue(router.reverse(FooController.class).contains(FQNBasedRouter.CONTROLLER_NAME));
        assertTrue(router.reverse(FooController.class).contains(FooController.class.getCanonicalName()));
    }

    @Test
    public void testRouterListing() {
        assertTrue(router.getAllControllers().isEmpty());
    }

    @Test
    public void testNullControllerName() throws ApplicationException {
        assertNull(router.resolve(context));
    }

    @Test
    public void testControllerNoDefaultConstructor() throws ApplicationException {
        String className = NoDefaultConstructorController.class.getCanonicalName();
        when(context.getRequest().getParameter(FQNBasedRouter.CONTROLLER_NAME)).thenReturn(className);
        assertNull(router.resolve(context));
    }

}
