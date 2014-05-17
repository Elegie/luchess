package io.elegie.luchess.web.framework.presenting.freemarker;

import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.HTML;
import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.JSON;
import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class FreemarkerViewOptionsTest {

    private static final String VIEW_TEXT = FreemarkerViewOptions.createViewName("foo", TEXT);
    private static final String VIEW_JSON = FreemarkerViewOptions.createViewName("foo", JSON);
    private static final String VIEW_HTML = FreemarkerViewOptions.createViewName("foo", HTML);
    private static final String VIEW_UNKNOWN_TYPE = "foo.ftl";
    private static final String VIEW_ATTACHMENT_YES = FreemarkerViewOptions.createViewName("foo", TEXT, true, false);
    private static final String VIEW_ATTACHMENT_NO = FreemarkerViewOptions.createViewName("foo", TEXT, false, false);
    private static final String VIEW_NOCACHE_YES = FreemarkerViewOptions.createViewName("foo", TEXT, false, true);
    private static final String VIEW_NOCACHE_NO = FreemarkerViewOptions.createViewName("foo", TEXT, true, false);

    @Test
    public void testViewName() {
        assertEquals(VIEW_TEXT, new FreemarkerViewOptions(VIEW_TEXT).getViewName());
    }

    @Test
    public void testContentType() {
        assertEquals(HTML.getType(), new FreemarkerViewOptions(VIEW_HTML).getContentType());
        assertEquals(JSON.getType(), new FreemarkerViewOptions(VIEW_JSON).getContentType());
        assertEquals(TEXT.getType(), new FreemarkerViewOptions(VIEW_TEXT).getContentType());
    }

    @Test(expected = IllegalStateException.class)
    public void testContentTypeUnknown() {
        new FreemarkerViewOptions(VIEW_UNKNOWN_TYPE).getContentType();
    }

    @Test
    public void testAttachment() {
        assertTrue(new FreemarkerViewOptions(VIEW_ATTACHMENT_YES).isAttachment());
        assertFalse(new FreemarkerViewOptions(VIEW_ATTACHMENT_NO).isAttachment());
    }

    @Test
    public void testNoCache() {
        assertTrue(new FreemarkerViewOptions(VIEW_NOCACHE_YES).isNoCache());
        assertFalse(new FreemarkerViewOptions(VIEW_NOCACHE_NO).isNoCache());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateViewNameNullName() {
        FreemarkerViewOptions.createViewName(null, TEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateViewNameNullType() {
        FreemarkerViewOptions.createViewName("foo", null);
    }

}
