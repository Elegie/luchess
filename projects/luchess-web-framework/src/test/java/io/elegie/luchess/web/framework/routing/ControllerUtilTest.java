package io.elegie.luchess.web.framework.routing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.web.framework.routing.controllers.BarController;
import io.elegie.luchess.web.framework.routing.controllers.FooController;
import io.elegie.luchess.web.framework.routing.controllers.SomeAbstractController;

import java.util.List;

import org.junit.Test;

/**
 * Test that controllers can be identified and retrieved from a package.
 */
@SuppressWarnings("javadoc")
public class ControllerUtilTest {

    @Test
    public void testFindControllersByPackage() {
        String packageName = FooController.class.getPackage().getName();
        List<Class<?>> controllers = ControllerUtil.findControllersByPackage(packageName);
        assertTrue(controllers.contains(FooController.class));
        assertTrue(controllers.contains(BarController.class));
        assertFalse(controllers.contains(SomeAbstractController.class));
    }

    @Test
    public void testFindNoControllersInUnknownPackage() {
        assertTrue(ControllerUtil.findControllersByPackage("unknown package").isEmpty());
    }
}
