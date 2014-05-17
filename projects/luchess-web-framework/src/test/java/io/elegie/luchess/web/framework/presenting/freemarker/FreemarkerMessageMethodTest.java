package io.elegie.luchess.web.framework.presenting.freemarker;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Test;

import freemarker.template.SimpleDate;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateModelException;

/**
 * Given a bundle, tests that the messages are properly retrieved, with
 * appropriate substitutions for texts, numbers and dates, matching the format
 * specified by the locale of the bundle.
 */
@SuppressWarnings("javadoc")
public class FreemarkerMessageMethodTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMissingKey() throws TemplateModelException {
        ResourceBundle bundle = new SimpleBundle("foo", "bar");
        FreemarkerMessageMethod method = new FreemarkerMessageMethod(bundle);
        method.exec(Arrays.asList("baz"));
    }

    @Test
    public void testSimpleTextTransation() throws TemplateModelException {
        ResourceBundle bundle = new SimpleBundle("foo", "bar");
        FreemarkerMessageMethod method = new FreemarkerMessageMethod(bundle);
        assertEquals("bar", method.exec(Arrays.asList("foo")));
    }

    @Test
    public void testParamTranslation() throws TemplateModelException, ParseException {
        ResourceBundle bundle = new SimpleBundle("foo", "bar {0} {1} {2}");
        FreemarkerMessageMethod method = new FreemarkerMessageMethod(bundle);
        SimpleNumber simpleNumber = new SimpleNumber(1234567);
        Date date = new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2014-03-14 13:11").getTime());
        SimpleDate simpleDate = new SimpleDate(date);
        assertEquals("bar baz 1,234,567 3/14/14 1:11 PM", method.exec(Arrays.asList("foo", "baz", simpleNumber, simpleDate)));
    }

    // --- Simple bundle implementation ---------------------------------------

    private static class SimpleBundle extends ResourceBundle {
        private String bundleKey;
        private String bundleValue;

        public SimpleBundle(String bundleKey, String bundleValue) {
            this.bundleKey = bundleKey;
            this.bundleValue = bundleValue;
        }

        @Override
        protected Object handleGetObject(String key) {
            return this.bundleKey.equals(key) ? bundleValue : null;
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(keySet());
        }

        @Override
        protected Set<String> handleKeySet() {
            return new HashSet<>(Arrays.asList(bundleKey));
        }

        @Override
        public Locale getLocale() {
            return Locale.ENGLISH;
        }
    }

}
