package io.elegie.luchess.web.client;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests that a client context can be properly created and configured, and that
 * proper exceptions are thrown upon configuration errors.
 */
@SuppressWarnings("javadoc")
public class ClientContextTest {

    private static final String EMPTY_FILE = "empty.properties";
    private static final String LUCHESS_WEB_FILE = "luchess-web-test.properties";
    private static final String LUCHESS_APP_FILE = "luchess-app-test.properties";
    private static final String LUCHESS_APP_FILE_UNINSTANTIABLE = "luchess-app-test-uninstantiable.properties";

    private Properties configuration;

    public static Properties createTestConfiguration() {
        Properties configuration = new Properties();
        configuration.put(ClientContext.LUCHESS_APP_FILE, LUCHESS_APP_FILE);
        configuration.put(ClientContext.LUCHESS_WEB_FILE, LUCHESS_WEB_FILE);
        return configuration;
    }

    @Before
    public void setUp() {
        configuration = createTestConfiguration();
    }

    @Test
    public void testConfiguration() throws ClientContextException {
        ClientContext context = ClientContext.INSTANCE;
        context.configure(configuration);
        assertNotNull(context.getRouter());
        assertNotNull(context.getPresenter());
        assertNotNull(context.getServicesFactory());
        assertNotNull(context.getMonitor());
        assertNotNull(context.getDataSet());
    }

    @Test(expected = ClientContextException.class)
    public void testConfigurationNull() throws ClientContextException {
        ClientContext.INSTANCE.configure(null);
    }

    @Test(expected = ClientContextException.class)
    public void testConfigurationUnknownFile() throws ClientContextException {
        configuration.put(ClientContext.LUCHESS_WEB_FILE, "foo");
        ClientContext.INSTANCE.configure(configuration);
    }

    @Test(expected = ClientContextException.class)
    public void testConfigurationWebFailureEmpty() throws ClientContextException {
        configuration.put(ClientContext.LUCHESS_WEB_FILE, EMPTY_FILE);
        ClientContext.INSTANCE.configure(configuration);
    }

    @Test(expected = ClientContextException.class)
    public void testConfigurationAppFailureEmpty() throws ClientContextException {
        configuration.put(ClientContext.LUCHESS_APP_FILE, EMPTY_FILE);
        ClientContext.INSTANCE.configure(configuration);
    }

    @Test(expected = ClientContextException.class)
    public void testConfigurationAppFailureUninstantiable() throws ClientContextException {
        configuration.put(ClientContext.LUCHESS_APP_FILE, LUCHESS_APP_FILE_UNINSTANTIABLE);
        ClientContext.INSTANCE.configure(configuration);
    }

}
