package io.elegie.luchess.web.framework.routing.pathbased;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.routing.controllers.BarController;
import io.elegie.luchess.web.framework.routing.controllers.FooController;
import io.elegie.luchess.web.framework.routing.controllers.MalformedControllerBadSuffix;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests that controllers can be inferred / build into URI paths.
 */
@SuppressWarnings("javadoc")
public class PathBasedRouterTest {

    private static final String CONTROLLERS_PACKAGE_NAME = FooController.class.getPackage().getName();
    private static final String CONTROLLERS_DEFAULT_NAME = FooController.class.getSimpleName();

    private PathBasedRouter router;
    private WebContext context;

    @Before
    public void setUp() {
        router = new PathBasedRouter(CONTROLLERS_PACKAGE_NAME, CONTROLLERS_DEFAULT_NAME);
        context = new WebContext(mock(HttpServletRequest.class), null);
    }

    @Test
    public void testRouterResolvingDefaultController() throws ApplicationException {
        when(context.getRequest().getPathInfo()).thenReturn("/");
        assertTrue(router.resolve(context) instanceof FooController);
    }

    @Test
    public void testRouterResolvingNamedController() throws ApplicationException {
        when(context.getRequest().getPathInfo()).thenReturn("/bar");
        assertTrue(router.resolve(context) instanceof BarController);
    }

    @Test(expected = ApplicationException.class)
    public void testRouterResolvingUnknownController() throws ApplicationException {
        when(context.getRequest().getPathInfo()).thenReturn("/" + UUID.randomUUID().toString());
        router.resolve(context);
    }

    @Test(expected = ApplicationException.class)
    public void testRouterResolvingNotController() throws ApplicationException {
        when(context.getRequest().getPathInfo()).thenReturn("/not/a");
        router.resolve(context);
    }

    @Test
    public void testRouterReversing() {
        assertEquals("/foo/", router.reverse(FooController.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRouterReversingBadSuffix() {
        router.reverse(MalformedControllerBadSuffix.class);
    }

    @Test
    public void testRouterListing() {
        assertTrue(router.getAllControllers().contains(FooController.class));
        assertTrue(router.getAllControllers().contains(BarController.class));
    }

    @Test(expected = ApplicationException.class)
    public void testControllerNoDefaultConstructor() throws ApplicationException {
        when(context.getRequest().getPathInfo()).thenReturn("/no/default/constructor");
        router.resolve(context);
    }
}
