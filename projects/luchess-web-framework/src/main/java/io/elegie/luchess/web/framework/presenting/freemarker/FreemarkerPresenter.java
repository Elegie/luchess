package io.elegie.luchess.web.framework.presenting.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.View;

/**
 * A presenter using the <a href="http://freemarker.org/">Freemarker Template
 * Engine</a>. All provided template files must be encoded in UTF-8.
 */
public class FreemarkerPresenter implements Presenter {

    /**
     * The key holding the path to the messages bundle. Conveniently stored in
     * the configuration custom attributes.
     */
    static final String MESSAGES_PATH_KEY = "messagesPathKey";

    private Configuration configuration;

    /**
     * @param viewsPath
     *            The path to the views. Must be located in the classpath. Ex:
     *            "/views"
     * @param messagesPath
     *            The path to the message bundles, including the file name root
     *            (without its locator and extension). Must be located in the
     *            classpath. Ex: "/messages/messages"
     */
    public FreemarkerPresenter(String viewsPath, String messagesPath) {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), viewsPath);
        configuration.setCustomAttribute(MESSAGES_PATH_KEY, messagesPath);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }

    @Override
    public View resolve(String viewName) {
        return new FreemarkerView(configuration, new FreemarkerViewOptions(viewName));
    }

}
