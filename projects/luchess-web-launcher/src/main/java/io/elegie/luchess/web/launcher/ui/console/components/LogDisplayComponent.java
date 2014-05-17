package io.elegie.luchess.web.launcher.ui.console.components;

import io.elegie.luchess.web.launcher.ui.UiComponent;
import io.elegie.luchess.web.launcher.ui.common.UiTextArea;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Component used to display the content of the logs (like a regular system
 * console). We simply capture the top logger information, and display it in a
 * text area.
 */
@SuppressWarnings("javadoc")
public class LogDisplayComponent implements UiComponent {

    private UiTextArea textArea;

    public LogDisplayComponent() {
        textArea = new UiTextArea();
        textArea.setName(LogDisplayComponent.class.getSimpleName());
        textArea.getContent().setEditable(false);

        LogDisplayAppender logger = new LogDisplayAppender(textArea.getContent());
        logger.init();
    }

    public void setVisible(boolean visible) {
        textArea.setVisible(visible);
    }

    public boolean isVisible() {
        return textArea.isVisible();
    }

    @Override
    public JComponent getComponent() {
        return textArea;
    }

    // ------------------------------------------------------------------------

    private static class LogDisplayAppender extends AppenderBase<ILoggingEvent> {
        private static final String FILTER_LOGGER_PROP = "filterLogger";
        private static final String NOPEX_PATTERN_PROP = "nopexPattern";
        private static final String STACK_PATTERN_PROP = "stacktracePattern";

        private JTextArea console;
        private PatternLayout nopexPattern;
        private PatternLayout stackPattern;

        public LogDisplayAppender(JTextArea console) {
            this.console = console;
        }

        public void init() {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            registerLogger(loggerContext);
            createLayouts(loggerContext);
        }

        private void registerLogger(LoggerContext loggerContext) {
            setContext(loggerContext);
            start();

            String loggerName = loggerContext.getProperty(FILTER_LOGGER_PROP);
            if (loggerName == null) {
                loggerName = Logger.ROOT_LOGGER_NAME;
            }
            loggerContext.getLogger(loggerName).addAppender(this);
        }

        private void createLayouts(LoggerContext loggerContext) {
            String nopexPatternProp = loggerContext.getProperty(NOPEX_PATTERN_PROP);
            if (nopexPatternProp != null) {
                nopexPattern = createPatternLayout(loggerContext, nopexPatternProp);
            }

            String stacktracePatternProp = loggerContext.getProperty(STACK_PATTERN_PROP);
            if (stacktracePatternProp != null) {
                stackPattern = createPatternLayout(loggerContext, stacktracePatternProp);
            }
        }

        private PatternLayout createPatternLayout(LoggerContext loggerContext, String pattern) {
            PatternLayout patternLayout = new PatternLayout();
            patternLayout.setPattern(pattern);
            patternLayout.setContext(loggerContext);
            patternLayout.start();
            return patternLayout;
        }

        @Override
        protected void append(ILoggingEvent event) {
            if (nopexPattern != null) {
                this.console.append(nopexPattern.doLayout(event));
                if (stackPattern != null && event.getLevel().isGreaterOrEqual(Level.ERROR)) {
                    this.console.append(stackPattern.doLayout(event));
                }
            } else {
                this.console.append(event.getFormattedMessage() + '\n');
            }
        }

    }
}
