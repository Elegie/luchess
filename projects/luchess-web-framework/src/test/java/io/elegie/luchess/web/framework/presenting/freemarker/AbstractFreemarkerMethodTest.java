package io.elegie.luchess.web.framework.presenting.freemarker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateModelException;

@SuppressWarnings("javadoc")
public class AbstractFreemarkerMethodTest {

    @Test
    public void testExactlyOneArgumentSuccess() throws TemplateModelException {
        new SimpleFreemarkerMethod().testExactlyOneArgument(Arrays.asList("foo"));
    }

    @Test(expected = TemplateModelException.class)
    public void testExactlyOneArgumentFailure() throws TemplateModelException {
        new SimpleFreemarkerMethod().testExactlyOneArgument(Arrays.asList("foo", "bar"));
    }

    @Test
    public void testAtLeastOneArgumentSuccess() throws TemplateModelException {
        new SimpleFreemarkerMethod().testAtLeastOneArgument(Arrays.asList("foo"));
    }

    @Test(expected = TemplateModelException.class)
    public void testAtLeastOneArgumentFailure() throws TemplateModelException {
        new SimpleFreemarkerMethod().testAtLeastOneArgument(Collections.EMPTY_LIST);
    }

    @Test
    public void testArgumentIsNotNullSuccess() throws TemplateModelException {
        new SimpleFreemarkerMethod().testArgumentIsNotNull("foo");
    }

    @Test(expected = TemplateModelException.class)
    public void testArgumentIsNotNullFailure() throws TemplateModelException {
        new SimpleFreemarkerMethod().testArgumentIsNotNull(null);
    }

    @Test
    public void testArgumentIsNumberSuccess() throws TemplateModelException {
        new SimpleFreemarkerMethod().testArgumentIsNumber(new SimpleNumber(42));
    }

    @Test(expected = TemplateModelException.class)
    public void testArgumentIsNumberFailure() throws TemplateModelException {
        new SimpleFreemarkerMethod().testArgumentIsNumber("foo");
    }

    // --- Helpers ------------------------------------------------------------

    private static class SimpleFreemarkerMethod extends AbstractFreemarkerMethod {
        @Override
        public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
            return null;
        }
    }

}
