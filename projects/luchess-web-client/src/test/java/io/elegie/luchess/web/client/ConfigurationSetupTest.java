package io.elegie.luchess.web.client;


import java.util.Properties;

import org.junit.Before;

/**
 * Initializes a test context for all our controller tests.
 */
@SuppressWarnings("javadoc")
public abstract class ConfigurationSetupTest {

    private static final String LUCHESS_APP_FILE = "luchess-app-test-integrated.properties";
    private static final String LUCHESS_WEB_FILE = "luchess-web-test.properties";

    @Before
    public void setUp() throws ClientContextException {
        Properties configuration = new Properties();
        configuration.put(ClientContext.LUCHESS_APP_FILE, LUCHESS_APP_FILE);
        configuration.put(ClientContext.LUCHESS_WEB_FILE, LUCHESS_WEB_FILE);
        ClientContext.INSTANCE.configure(configuration);
    }

}
