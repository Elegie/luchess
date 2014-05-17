package io.elegie.luchess.web.framework.presenting.fqnbased;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class FQNBasedPresenterTest {

    @Test
    public void testViewResolving() {
        assertTrue(new FQNBasedPresenter().resolve(FQNBasedViewHelper.class.getCanonicalName()) instanceof FQNBasedViewHelper);
    }

    @Test
    public void testNullViewResolving() {
        assertNull(new FQNBasedPresenter().resolve(null));
    }

    @Test
    public void testUnknownViewResolving() {
        assertNull(new FQNBasedPresenter().resolve("Hello, World!"));
    }

}
