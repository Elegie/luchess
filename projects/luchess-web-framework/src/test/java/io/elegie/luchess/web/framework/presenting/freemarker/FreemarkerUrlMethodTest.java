package io.elegie.luchess.web.framework.presenting.freemarker;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import freemarker.template.TemplateModelException;

/**
 * Tests that URL are properly constructed, with the context path being
 * prepended.
 */
@SuppressWarnings("javadoc")
public class FreemarkerUrlMethodTest {

    @Test
    public void testUrlBuilding() throws TemplateModelException {
        assertEquals("/foo/bar", new FreemarkerUrlMethod("/foo/").exec(Arrays.asList("bar")));
    }

}
