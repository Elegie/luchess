package io.elegie.luchess.web.framework.presenting.freemarker;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import freemarker.template.SimpleDate;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateModelException;

/**
 * This method is used to translate a message key inside a template, into a
 * localized text, using underlying resources bundles. Numbers and dates are
 * also properly localized.
 */
public class FreemarkerMessageMethod extends AbstractFreemarkerMethod {

    private ResourceBundle bundle;

    /**
     * @param bundle
     *            From which to read the translated text.
     */
    public FreemarkerMessageMethod(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        testAtLeastOneArgument(arguments);
        String value = readValue(arguments.get(0));
        MessageFormat formatter = new MessageFormat(value, bundle.getLocale());
        if (arguments.size() == 1) {
            value = formatter.format(new Object[] {});
        } else {
            List<Object> parameters = new ArrayList<>();
            for (Object obj : arguments.subList(1, arguments.size())) {
                if (obj instanceof SimpleNumber) {
                    parameters.add(((SimpleNumber) obj).getAsNumber());
                } else if (obj instanceof SimpleDate) {
                    parameters.add(((SimpleDate) obj).getAsDate());
                } else {
                    parameters.add(obj.toString());
                }
            }
            value = formatter.format(parameters.toArray(), new StringBuffer(), null).toString();
        }
        return value;
    }

    private String readValue(Object arg) throws TemplateModelException {
        testArgumentIsNotNull(arg);
        String key = arg.toString();
        if (!bundle.containsKey(key)) {
            String message = "No matching entry found in the bundle for \"%s\"";
            message = String.format(message, key);
            throw new IllegalArgumentException(message);
        }
        return bundle.getString(key);
    }
}
