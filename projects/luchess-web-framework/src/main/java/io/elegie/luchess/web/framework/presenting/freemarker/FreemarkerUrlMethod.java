package io.elegie.luchess.web.framework.presenting.freemarker;

import java.util.List;

import freemarker.template.TemplateModelException;

/**
 * Provides a method which can be used in templates, in charge of building
 * accessible URLs, prepending the context path.
 */
public class FreemarkerUrlMethod extends AbstractFreemarkerMethod {

    private String contextPath;

    /**
     * @param contextPath
     *            Path of the application, to be prepended to URLs.
     */
    public FreemarkerUrlMethod(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return contextPath + readArg(arguments);
    }

    private String readArg(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        testExactlyOneArgument(arguments);
        Object arg = arguments.get(0);
        testArgumentIsNotNull(arg);
        return arg.toString();
    }
}