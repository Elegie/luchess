package io.elegie.luchess.web.launcher.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * All messages used by the application. The value of each message is the key to
 * be used in the bundle files.
 */
@SuppressWarnings("javadoc")
public enum UiMessages {
    TITLE("title"),
    WELCOME("welcome"),

    SERVER_START("serverStart"),
    SERVER_STOP("serverStop"),

    DETAIL_SHOW("detailShow"),
    DETAIL_HIDE("detailHide"),
    WEB_APP_MISSING("webAppMissing");

    private String value;

    private UiMessages(String value) {
        this.value = value;
    }

    /**
     * @return The key to be used in the bundle files.
     */
    public String getValue() {
        return this.value;
    }

    // ------------------------------------------------------------------------

    /**
     * The location and root name of bundle files: {@value #BUNDLE_BASE_NAME}.
     * Bundle files must be encoded in UTF-8.
     */
    public static final String BUNDLE_BASE_NAME = "messages/messages";

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, new UTF8Control());

    /**
     * @param key
     *            The key for which we want a localized text.
     * @return The localized text.
     */
    public static String get(UiMessages key) {
        return resourceBundle.getString(key.getValue());
    }

    private static class UTF8Control extends Control {
        @SuppressWarnings("resource")
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }

}
