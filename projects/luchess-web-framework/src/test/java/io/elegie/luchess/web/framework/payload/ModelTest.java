package io.elegie.luchess.web.framework.payload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ModelTest {

    @Test
    public void testModelFilling() {
        Model model = new Model();
        Object key = new Object();
        Object value = new Object();
        model.put(key, value);
        assertEquals(value, model.get(key));
    }

    @Test
    public void testEqualsHashcode() {
        Model model1 = new Model();
        Model model2 = new Model();
        assertEquals(model1, model1);
        assertFalse(model1.equals("foo"));
        assertEquals(model1, model2);
        assertEquals(model1.hashCode(), model2.hashCode());
    }
}
