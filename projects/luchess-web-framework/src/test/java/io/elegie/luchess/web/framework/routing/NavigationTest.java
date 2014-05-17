package io.elegie.luchess.web.framework.routing;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.payload.WebContext;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the navigation finds the proper controller, looking up the name of
 * the controller.
 */
@SuppressWarnings("javadoc")
public class NavigationTest {

    private Navigation navigation;

    @Before
    public void setUp() {
        navigation = new Navigation(new NavigationRouterHelper());
    }

    @Test
    public void testNavigationSuccess() {
        navigation.go(NavigationControllerHelper.class.getSimpleName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNavigationFailure() {
        navigation.go("foo");
    }

    // --- Helpers ------------------------------------------------------------

    private static class NavigationControllerHelper {
        @Controller
        public Result control() {
            return null;
        }
    }

    private static class NavigationRouterHelper implements Router {
        @Override
        public Object resolve(WebContext context) throws ApplicationException {
            return new NavigationControllerHelper();
        }

        @Override
        public String reverse(Class<?> controller) {
            if (controller.equals(NavigationControllerHelper.class)) {
                return NavigationControllerHelper.class.getSimpleName();
            }
            return null;
        }

        @Override
        public List<Class<?>> getAllControllers() {
            Class<?> controllerClass = NavigationControllerHelper.class;
            List<Class<?>> classes = new ArrayList<>();
            classes.add(controllerClass);
            return classes;
        }
    }

}
