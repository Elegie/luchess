package io.elegie.luchess.web.framework.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.exceptions.ApplicationException;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.payload.WebContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

/**
 * Tests that a web request can be properly processed, i.e. that the parameter
 * can be built from the web context, and passed to the proper controller
 * method.
 */
@SuppressWarnings("javadoc")
public class WebRequestTest {

    @Test
    public void testControllerNoArg() throws ApplicationException {
        new WebRequest(mock(WebContext.class)).process(new SimpleControllerNoArg());
    }

    @Test
    public void testControllerSimpleArg() throws ApplicationException {
        String intValue = "42";
        String longValue = "100";
        String floatValue = "25.5";
        String doubleValue = "42.2";
        String booleanValue1 = "true";
        String booleanValue2 = "1";
        String intObjValue = "56";
        String longObjValue = "89";
        String floatObjValue = "98.8";
        String doubleObjValue = "100.2";
        String booleanObjValue1 = "false";
        String booleanObjValue2 = "0";
        String stringValue = "foo";
        String listValue1 = "list1";
        String listValue2 = "list2";
        String setValue1 = "set1";
        String setValue2 = "set2";

        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("intValue", new String[] { intValue });
        parameterMap.put("longValue", new String[] { longValue });
        parameterMap.put("floatValue", new String[] { floatValue });
        parameterMap.put("doubleValue", new String[] { doubleValue });
        parameterMap.put("booleanValue1", new String[] { booleanValue1 });
        parameterMap.put("booleanValue2", new String[] { booleanValue2 });
        parameterMap.put("intObjValue", new String[] { intObjValue });
        parameterMap.put("longObjValue", new String[] { longObjValue });
        parameterMap.put("floatObjValue", new String[] { floatObjValue });
        parameterMap.put("doubleObjValue", new String[] { doubleObjValue });
        parameterMap.put("booleanObjValue1", new String[] { booleanObjValue1 });
        parameterMap.put("booleanObjValue2", new String[] { booleanObjValue2 });
        parameterMap.put("stringValue", new String[] { stringValue });
        parameterMap.put("listValue", new String[] { listValue1, listValue2 });
        parameterMap.put("setValue", new String[] { setValue1, setValue2 });
        WebContext context = createContext(parameterMap);

        Result result = new WebRequest(context).process(new SimpleControllerWithArg());
        SimpleParameter parameter = (SimpleParameter) result.getModel().get(SimpleControllerWithArg.PARAM_KEY);
        assertEquals(intValue, Integer.toString(parameter.getIntValue()));
        assertEquals(longValue, Long.toString(parameter.getLongValue()));
        assertEquals(floatValue, Float.toString(parameter.getFloatValue()));
        assertEquals(doubleValue, Double.toString(parameter.getDoubleValue()));
        assertTrue(parameter.getBooleanValue1());
        assertTrue(parameter.getBooleanValue2());
        assertEquals(intObjValue, parameter.getIntObjValue().toString());
        assertEquals(longObjValue, parameter.getLongObjValue().toString());
        assertEquals(floatObjValue, parameter.getFloatObjValue().toString());
        assertEquals(doubleObjValue, parameter.getDoubleObjValue().toString());
        assertFalse(parameter.getBooleanObjValue1());
        assertFalse(parameter.getBooleanObjValue2());
        assertEquals(stringValue, parameter.getStringValue());
        assertTrue(parameter.getListValue().contains(listValue1));
        assertTrue(parameter.getListValue().contains(listValue2));
        assertTrue(parameter.getSetValue().contains(setValue1));
        assertTrue(parameter.getSetValue().contains(setValue2));
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testNullWebContext() {
        new WebRequest(null);
    }

    @Test(expected = ApplicationException.class)
    public void testNotController() throws ApplicationException {
        new WebRequest(mock(WebContext.class)).process(new NotController());
    }

    @Test(expected = ApplicationException.class)
    public void testCannotCreateParameter() throws ApplicationException {
        new WebRequest(mock(WebContext.class)).process(new SimpleControllerWithUninstantiableArg());
    }

    @Test
    public void testCannotConvertLiteral() throws ApplicationException {
        assertEquals(0, processSimpleControllerWithArgWithSingleParam("intValue", "notAnInt").getIntValue());
    }

    @Test
    public void testCannotConvertObject() throws ApplicationException {
        assertEquals(null, processSimpleControllerWithArgWithSingleParam("intObjValue", "notAnInt").getIntObjValue());
    }

    @Test
    public void testCannotConvertBoolean() throws ApplicationException {
        assertEquals(null, processSimpleControllerWithArgWithSingleParam("booleanObjValue1", "notABoolean").getBooleanObjValue1());
    }

    @Test
    public void testFeedCollectionUnassignableParameter() throws ApplicationException {
        String listValue1 = "list1";
        String listValue2 = "list2";
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("listValue", new String[] { listValue1, listValue2 });
        WebContext context = createContext(parameterMap);
        new WebRequest(context).process(new SimpleControllerWithUnassignableList());
    }

    private WebContext createContext(Map<String, String[]> parameterMap) {
        WebContext context = mock(WebContext.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(context.getRequest()).thenReturn(request);
        when(request.getParameterMap()).thenReturn(parameterMap);
        return context;
    }

    private SimpleParameter processSimpleControllerWithArgWithSingleParam(String property, String value)
            throws ApplicationException {
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put(property, new String[] { value });
        WebContext context = createContext(parameterMap);

        Result result = new WebRequest(context).process(new SimpleControllerWithArg());
        return (SimpleParameter) result.getModel().get(SimpleControllerWithArg.PARAM_KEY);
    }

    // --- Helpers ------------------------------------------------------------

    private static class NotController {
    }

    private static class SimpleControllerNoArg {
        @Controller
        public Result foo() {
            return null;
        }
    }

    private static class SimpleControllerWithArg {
        public static final String PARAM_KEY = "parameter";

        @Controller
        public Result foo(SimpleParameter parameter) {
            Model model = new Model();
            model.put(PARAM_KEY, parameter);
            return new Result("viewName", model);
        }
    }

    private static class SimpleControllerWithUninstantiableArg {
        @Controller
        public Result foo(@SuppressWarnings("unused") UninstantiableSimpleParameter parameter) {
            return null;
        }
    }

    private static class SimpleControllerWithUnassignableList {
        @Controller
        public Result foo(@SuppressWarnings("unused") SimpleParameterUnassignableList parameter) {
            return null;
        }
    }

    static class SimpleParameter {
        private int intValue;
        private long longValue;
        private float floatValue;
        private double doubleValue;
        private boolean booleanValue1;
        private boolean booleanValue2;
        private Integer intObjValue;
        private Long longObjValue;
        private Float floatObjValue;
        private Double doubleObjValue;
        private Boolean booleanObjValue1;
        private Boolean booleanObjValue2;
        private String stringValue;
        private List<String> listValue;
        private Set<String> setValue;

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public boolean getBooleanValue1() {
            return booleanValue1;
        }

        public void setBooleanValue1(boolean booleanValue) {
            this.booleanValue1 = booleanValue;
        }

        public boolean getBooleanValue2() {
            return booleanValue2;
        }

        public void setBooleanValue2(boolean booleanValue) {
            this.booleanValue2 = booleanValue;
        }

        public Integer getIntObjValue() {
            return intObjValue;
        }

        public void setIntObjValue(Integer intObjValue) {
            this.intObjValue = intObjValue;
        }

        public Long getLongObjValue() {
            return longObjValue;
        }

        public void setLongObjValue(Long longObjValue) {
            this.longObjValue = longObjValue;
        }

        public Float getFloatObjValue() {
            return floatObjValue;
        }

        public void setFloatObjValue(Float floatObjValue) {
            this.floatObjValue = floatObjValue;
        }

        public Double getDoubleObjValue() {
            return doubleObjValue;
        }

        public void setDoubleObjValue(Double doubleObjValue) {
            this.doubleObjValue = doubleObjValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public Boolean getBooleanObjValue1() {
            return booleanObjValue1;
        }

        public void setBooleanObjValue1(Boolean booleanObjValue) {
            this.booleanObjValue1 = booleanObjValue;
        }

        public Boolean getBooleanObjValue2() {
            return booleanObjValue2;
        }

        public void setBooleanObjValue2(Boolean booleanObjValue) {
            this.booleanObjValue2 = booleanObjValue;
        }

        public List<String> getListValue() {
            return listValue;
        }

        public void setListValue(List<String> listValue) {
            this.listValue = listValue;
        }

        public Set<String> getSetValue() {
            return setValue;
        }

        public void setSetValue(Set<String> setValue) {
            this.setValue = setValue;
        }
    }

    private static class UninstantiableSimpleParameter {
        // Because it's private
    }

    static class SimpleParameterUnassignableList {
        private List<Object> listValue;

        public List<Object> getListValue() {
            return listValue;
        }

        public void setListValue(List<Object> listValue) {
            this.listValue = listValue;
        }
    }

}
