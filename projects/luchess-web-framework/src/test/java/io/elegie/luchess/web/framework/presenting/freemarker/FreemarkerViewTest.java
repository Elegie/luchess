package io.elegie.luchess.web.framework.presenting.freemarker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Run integrated testing for the display of the view, making sure that every
 * FTL function be injected and usable, that variables be properly extrapolated,
 * and that HTTP headers be properly set.
 */
public class FreemarkerViewTest {

    private static final String VIEWS_PATH = "/views";
    private static final String MESSAGES_PATH = "messages/messagestest";
    private static final String VIEW_SIMPLE = "viewtest.txt.ftl";
    private static final String VIEW_NOCACHE_ATTACHMENT = "viewtest.nocache.attachment.txt.ftl";
    private static final String CONTEXT_PATH = "/context/path/";
    private static final String MODEL_KEY = "foo";
    private static final String MODEL_VALUE = "bar";
    private static final String MESSAGES_VALUE = "baz";
    private static final String URL_VALUE = "foo";
    private static final String DURATION = "01:00:00";
    private static final String FILE_NAME = "foo.bar";

    /**
     * Simple view rendering. We check that the variable content is rendered,
     * and that custom FTL functions are inserted, and callable.
     * 
     * @throws IOException
     *             When the framework cannot writer to the writer (cannot happen
     *             in this test).
     */
    @Test
    public void testIntegratedDisplay() throws IOException {
        Presenter presenter = new FreemarkerPresenter(VIEWS_PATH, MESSAGES_PATH);
        View view = presenter.resolve(VIEW_SIMPLE);
        StringWriter writer = new StringWriter();
        WebContext context = new WebContext(createRequest(), createResponse(writer));
        view.display(context, createModel());
        String content = writer.toString();

        assertTrue(content.contains(MODEL_VALUE));
        assertTrue(content.contains(MESSAGES_VALUE));
        assertTrue(content.contains(CONTEXT_PATH + URL_VALUE));
        assertTrue(content.contains(DURATION));
    }

    /**
     * Simple view rendered as an attachment, with no caching. We check that the
     * appropriate HTTP headers are set.
     * 
     * @throws IOException
     *             When the framework cannot writer to the writer (cannot happen
     *             in this test).
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testIntegratedDisplayWithNoCacheAndAttachment() throws IOException {
        Presenter presenter = new FreemarkerPresenter(VIEWS_PATH, MESSAGES_PATH);
        View view = presenter.resolve(VIEW_NOCACHE_ATTACHMENT);
        StringWriter writer = new StringWriter();
        HttpServletResponse resp = createResponse(writer);
        final Map<String, String> headers = new HashMap<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                String headerName = (String) args[0];
                String headerValue = (String) args[1];
                headers.put(headerName, headerValue);
                return null;
            }
        }).when(resp).setHeader(anyString(), anyString());

        WebContext context = new WebContext(createRequest(), resp);
        view.display(context, createModel());
        String content = writer.toString();

        assertTrue(content.contains(MODEL_VALUE));
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Disposition", "attachment;filename=" + FILE_NAME);
        expectedHeaders.put("Cache-Control", "no-cache, no-store, must-revalidate");
        expectedHeaders.put("Pragma", "no-cache");
        expectedHeaders.put("Expires", "0");
        for (Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            assertTrue(expectedHeaders.containsKey(key));
            assertEquals(expectedHeaders.get(key), entry.getValue());
        }
    }

    private Model createModel() {
        Model model = new Model();
        model.put(MODEL_KEY, MODEL_VALUE);
        model.put(FreemarkerView.ATTACHMENT_KEY, FILE_NAME);
        return model;
    }

    private HttpServletRequest createRequest() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getLocale()).thenReturn(Locale.ENGLISH);
        when(req.getContextPath()).thenReturn(CONTEXT_PATH);
        return req;
    }

    private HttpServletResponse createResponse(Writer writer) throws IOException {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        return resp;
    }
}
