package io.elegie.luchess.web.framework.payload;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class WebContextTest {

    @Test
    public void testWebContext() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        WebContext context = new WebContext(req, resp);
        assertEquals(req, context.getRequest());
        assertEquals(resp, context.getResponse());
    }

}
