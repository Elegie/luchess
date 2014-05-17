package io.elegie.luchess.web.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.framework.payload.Model;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ModelsTest {

    @Test
    public void testCreateEmptyModel() {
        assertEquals(new Model(), Models.createEmptyModel());
    }

    @Test
    public void testCreateModelWithNavigation() {
        assertNotNull(Models.createModelWithNavigation().get(Models.NAVIGATION.getValue()));
    }

}
