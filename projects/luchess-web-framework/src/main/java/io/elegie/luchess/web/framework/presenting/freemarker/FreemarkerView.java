package io.elegie.luchess.web.framework.presenting.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.WebContext;
import io.elegie.luchess.web.framework.presenting.View;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A view rendered thanks to the <a href="http://freemarker.org/">Freemarker
 * Template Engine</a>. Note that the view automatically makes available a
 * certain number of methods, which can be used in all templates.
 */
public class FreemarkerView implements View {

    /**
     * Name of the FTL function which can be used in templates to retrieve
     * localized texts: {@value #MESSAGE_KEY}.
     */
    public static final String MESSAGE_KEY = "msg";

    /**
     * Name of the FTL function which can be used in templates to normalize
     * URLs: {@value #URL_KEY}.
     */
    public static final String URL_KEY = "url";

    /**
     * Name of the FTL function which can be used in templates to normalize
     * durations: {@value #DURATION_KEY}.
     */
    public static final String DURATION_KEY = "dur";

    /**
     * Name of the key in the model, which holds the name of the attached file,
     * when present: {@value #ATTACHMENT_KEY}.
     */
    public static final String ATTACHMENT_KEY = "attachment";

    private Configuration configuration;
    private FreemarkerViewOptions options;

    /**
     * @param configuration
     *            The Freemarker configuration, which contains the cached
     *            templates.
     * @param options
     *            All options required to properly render the response, such as
     *            the content-type.
     */
    public FreemarkerView(Configuration configuration, FreemarkerViewOptions options) {
        this.configuration = configuration;
        this.options = options;
    }

    @Override
    public void display(WebContext context, Model model) {
        try {
            injectMessages(context, model);
            injectUrlMethod(context, model);
            injectDurationMethod(model);
            renderTemplate(context, model);
        } catch (IOException | TemplateException e) {
            String message = "Cannot process template.";
            throw new IllegalStateException(message, e);
        }
    }

    private void injectMessages(WebContext context, Model model) {
        HttpServletRequest request = context.getRequest();
        Locale locale = request.getLocale();
        String bundleName = (String) configuration.getCustomAttribute(FreemarkerPresenter.MESSAGES_PATH_KEY);
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, this.getClass().getClassLoader());
        model.put(MESSAGE_KEY, new FreemarkerMessageMethod(bundle));
    }

    private void injectUrlMethod(WebContext context, Model model) {
        HttpServletRequest request = context.getRequest();
        String contextPath = request.getContextPath();
        model.put(URL_KEY, new FreemarkerUrlMethod(contextPath));
    }

    private void injectDurationMethod(Model model) {
        model.put(DURATION_KEY, new FreemarkerDurationMethod());
    }

    private void renderTemplate(WebContext context, Model model) throws IOException, TemplateException {
        Template template = configuration.getTemplate(options.getViewName());
        HttpServletResponse response = context.getResponse();
        setContentType(response, template);
        setAttachment(response, model);
        setNoCache(response);
        template.process(model, response.getWriter());
    }

    private void setContentType(HttpServletResponse response, Template template) {
        response.setContentType(options.getContentType() + "; charset=" + template.getEncoding());
    }

    private void setAttachment(HttpServletResponse response, Model model) {
        if (options.isAttachment()) {
            String fileName = ATTACHMENT_KEY;
            Object fileNameObj = model.get(ATTACHMENT_KEY);
            if (fileNameObj != null) {
                fileName = fileNameObj.toString();
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        }
    }

    private void setNoCache(HttpServletResponse response) {
        if (options.isNoCache()) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
        }
    }
}