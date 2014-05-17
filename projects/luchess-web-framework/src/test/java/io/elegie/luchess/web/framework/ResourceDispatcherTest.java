package io.elegie.luchess.web.framework;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the navigation is done properly, depending on whether assets are
 * requested, or services.
 */
@SuppressWarnings("javadoc")
public class ResourceDispatcherTest {

    private static final String APP_PATH = "appPath/";
    private static final String ASSETS_PATH = "assetsPath/";

    private ResourceDispatcher dispatcher;
    private FilterConfig filterConfig;
    private FilterChain filterChain;

    @Before
    public void setUp() {
        dispatcher = new ResourceDispatcher();
        filterConfig = mock(FilterConfig.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    public void testFilter() throws ServletException, IOException {
        when(filterConfig.getInitParameter(ResourceDispatcher.ASSETS_KEY)).thenReturn(ASSETS_PATH);
        when(filterConfig.getInitParameter(ResourceDispatcher.APP_KEY)).thenReturn(APP_PATH);
        dispatcher.init(filterConfig);
        testAppRedirection();
        testAssetsRedirection();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingParamAppPath() throws ServletException {
        when(filterConfig.getInitParameter(ResourceDispatcher.ASSETS_KEY)).thenReturn(ASSETS_PATH);
        dispatcher.init(filterConfig);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingParamAssetsPath() throws ServletException {
        when(filterConfig.getInitParameter(ResourceDispatcher.APP_KEY)).thenReturn(APP_PATH);
        dispatcher.init(filterConfig);
    }

    private void testAppRedirection() throws IOException, ServletException {
        dispatcher.doFilter(createRequest("foo", APP_PATH + "foo"), null, filterChain);
    }

    private void testAssetsRedirection() throws IOException, ServletException {
        dispatcher.doFilter(createRequest(ASSETS_PATH + "foo", ASSETS_PATH + "foo"), null, filterChain);
    }

    private HttpServletRequest createRequest(String requestUri, String dispatcherUri) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        RequestDispatcher reqDispatcher = mock(RequestDispatcher.class);
        when(req.getRequestURI()).thenReturn(requestUri);
        when(req.getContextPath()).thenReturn("");
        when(req.getRequestDispatcher(dispatcherUri)).thenReturn(reqDispatcher);
        return req;
    }
}
