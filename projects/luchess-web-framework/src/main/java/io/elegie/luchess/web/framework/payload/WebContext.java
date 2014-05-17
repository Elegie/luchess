package io.elegie.luchess.web.framework.payload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An single object holding references to the web container objects, such as the
 * request and the response. This object has been introduced in order to keep
 * the APIs simple.
 */
@SuppressWarnings("javadoc")
public class WebContext {

    private HttpServletRequest req;
    private HttpServletResponse resp;

    public WebContext(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public HttpServletRequest getRequest() {
        return req;
    }

    public HttpServletResponse getResponse() {
        return resp;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {request: %s, response: %s}";
        return String.format(value, className, req, resp);
    }

}
