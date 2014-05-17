package io.elegie.luchess.web.framework.payload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ResultTest {

    @Test
    public void testResultCreation() {
        Model model = new Model();
        String viewName = "viewName";
        Result result = new Result(viewName, model);
        assertEquals(model, result.getModel());
        assertEquals(viewName, result.getViewName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullViewName() {
        @SuppressWarnings("unused")
        Result result = new Result(null, new Model());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyViewName() {
        @SuppressWarnings("unused")
        Result result = new Result("", new Model());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullModel() {
        @SuppressWarnings("unused")
        Result result = new Result("viewName", null);
    }

    @Test
    public void testEqualsHashcode() {
        Result result1 = new Result("viewName", new Model());
        Result result2 = new Result("viewName", new Model());
        assertEquals(result1, result1);
        assertFalse(result1.equals("foo"));
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());

        Model model = new Model();
        model.put("foo", "bar");
        assertFalse(result1.equals(new Result("viewName", model)));
        assertFalse(result1.equals(new Result("fooName", model)));
    }

}
