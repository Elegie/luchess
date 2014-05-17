package io.elegie.luchess.web.framework;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * This dispatcher is used as a front controller for all types of resources,
 * redirecting either to our applicative content (the front controller) or to
 * static content, depending on what type of resource is requested.
 * </p>
 * 
 * The redirection is based upon the requested URL. The filter is set up for all
 * incoming requests, then analyzes the prefix of the URL:
 * <ul>
 * <li>If it matches some asset URL, then it simply calls the next filter. If no
 * further filter is defined, then the requested content (the static resource)
 * is simply served as is.</li>
 * <li>if it does not match some asset URL, then we prepend a certain prefix,
 * and forward the request to the newly-built URL. The prepended prefix should
 * match a servlet mapping for the front servlet, so that the content be
 * processed by the application.</li>
 * </ul>
 * 
 * <p>
 * Both prefixes - asset and application - should be defined in the web.xml, in
 * the initialization parameters of the filter, also making sure that the
 * application prefix is also included in the front controller servlet mapping.
 * The client must specify the values (an URL path prefix) using the following
 * keys in the web.xml: {@value #APP_KEY} and {@value #ASSETS_KEY}.
 * </p>
 * 
 * <p>
 * Applying the filter to all URLs:
 * </p>
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;ResourceDispatcher&lt;/filter-name&gt;
 *   &lt;filter-class&gt;ResourceDispatcherClass&lt;/filter-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;framework.dispatcher.app&lt;/param-name&gt;
 *     &lt;param-value&gt;/app&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;framework.dispatcher.assets&lt;/param-name&gt;
 *     &lt;param-value&gt;/assets&lt;/param-value&gt;
 *   &lt;/init-param&gt;        
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;ResourceDispatcher&lt;/filter-name&gt;
 *   &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * 
 * <p>
 * Making sure the applicative front servlet matches the app key:
 * </p>
 * 
 * <pre>
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;FrontServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/app/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 */
public class ResourceDispatcher implements Filter {

    /**
     * Key to be used in the web.xml to specify the start of the path of
     * application pages: {@value #APP_KEY}.
     */
    public static final String APP_KEY = "framework.dispatcher.app";

    /**
     * Key to be used in the web.xml to specify the start of the path of static
     * content: : {@value #ASSETS_KEY}.
     */
    public static final String ASSETS_KEY = "framework.dispatcher.assets";

    private String appPath;
    private String assetsPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        appPath = readValue(filterConfig, APP_KEY);
        assetsPath = readValue(filterConfig, ASSETS_KEY);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith(assetsPath)) {
            chain.doFilter(request, response);
        } else {
            if (!path.startsWith(appPath)) {
                path = appPath + path;
            }
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

    @Override
    public void destroy() {
        // Nothing
    }

    private String readValue(FilterConfig filterConfig, String key) {
        String paramValue = filterConfig.getInitParameter(key);
        if (paramValue == null) {
            String message = "Missing path for key \"%s\".";
            message = String.format(message, key);
            throw new IllegalArgumentException(message);
        }
        return paramValue;
    }

}
