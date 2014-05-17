package io.elegie.luchess.web.framework.presenting.freemarker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateModelException;

/**
 * This method converts an integer value expressed in milliseconds, into a
 * properly formatted duration (hh:mm:ss).
 */
public class FreemarkerDurationMethod extends AbstractFreemarkerMethod {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTES = 60;

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        testExactlyOneArgument(arguments);

        Object arg = arguments.get(0);
        testArgumentIsNumber(arg);
        long millis = ((SimpleNumber) arg).getAsNumber().longValue();
        long hrs = TimeUnit.MILLISECONDS.toHours(millis);
        long min = TimeUnit.MILLISECONDS.toMinutes(millis) % MINUTES_PER_HOUR;
        long sec = TimeUnit.MILLISECONDS.toSeconds(millis) % SECONDS_PER_MINUTES;
        return String.format("%02d:%02d:%02d", hrs, min, sec);
    }
}
