package io.elegie.luchess.web.framework.presenting.freemarker;

import java.util.List;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * Provides methods to subclasses, that let them validate inputs.
 */
public abstract class AbstractFreemarkerMethod implements TemplateMethodModelEx {

    protected void testExactlyOneArgument(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            String message = "Expected exactly one argument, had %s.";
            message = String.format(message, arguments);
            throw new TemplateModelException(message);
        }
    }

    protected void testAtLeastOneArgument(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.isEmpty()) {
            String message = "Expected at least one argument, had %s.";
            message = String.format(message, arguments);
            throw new TemplateModelException(message);
        }
    }

    protected void testArgumentIsNotNull(Object arg) throws TemplateModelException {
        if (arg == null) {
            String message = "Argument may not be null.";
            throw new TemplateModelException(message);
        }
    }

    protected void testArgumentIsNumber(Object arg) throws TemplateModelException {
        if (!(arg instanceof SimpleNumber)) {
            String message = "Argument (%s) must be a number.";
            message = String.format(message, arg);
            throw new TemplateModelException(message);
        }
    }

}
