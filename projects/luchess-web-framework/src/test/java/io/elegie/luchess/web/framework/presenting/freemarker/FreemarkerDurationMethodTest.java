package io.elegie.luchess.web.framework.presenting.freemarker;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateModelException;

/**
 * Tests that the duration method properly converts a duration into a
 * human-readable format.
 */
@SuppressWarnings("javadoc")
public class FreemarkerDurationMethodTest {

    @Test
    public void testDurationFormat() throws TemplateModelException {
        long hours = 1 * 60 * 60 * 1000;
        long minutes = 2 * 60 * 1000;
        long seconds = 3 * 1000;
        SimpleNumber duration = new SimpleNumber(hours + minutes + seconds);
        assertEquals("01:02:03", new FreemarkerDurationMethod().exec(Arrays.asList(duration)).toString());
    }

}
