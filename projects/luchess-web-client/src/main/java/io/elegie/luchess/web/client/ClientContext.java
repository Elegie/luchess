package io.elegie.luchess.web.client;

import io.elegie.luchess.app.lucene4x.configuration.AppConfigPropertiesKeys;
import io.elegie.luchess.core.api.ServicesFactory;
import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.indexing.adapters.file.FileSourceDataSet;
import io.elegie.luchess.core.indexing.adapters.file.PGNFileSelector;
import io.elegie.luchess.web.client.models.BuildMonitor;
import io.elegie.luchess.web.framework.ApplicationContext;
import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerPresenter;
import io.elegie.luchess.web.framework.routing.Router;
import io.elegie.luchess.web.framework.routing.pathbased.PathBasedRouter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * <p>
 * Our own application context, which make domain objects available to
 * controllers (such as the services factory or the build monitor).
 * </p>
 * 
 * The context should be initialized with configuration data, which can be
 * retrieved from two external resources, which paths should be obtained from
 * the configuration properties passed to the {@link #configure(Properties)}
 * method :
 * <ul>
 * <li>The Luchess webapp configuration file, so as to configure the execution
 * flow of all requests. The key to be used is {@value #LUCHESS_WEB_FILE}.</li>
 * <li>The Luchess engine configuration file, so as to configure all services.
 * The key to be used is {@value #LUCHESS_APP_FILE}.</li>
 * </ul>
 * 
 * <p>
 * Both configuration files must be located inside the classpath.
 * </p>
 */
public enum ClientContext implements ApplicationContext {
    /**
     * Only one context per application, so we use a singleton.
     */
    INSTANCE;

    /**
     * The key to be used in the web.xml to reference the Luchess App
     * configuration file: {@value #LUCHESS_APP_FILE}.
     */
    public static final String LUCHESS_APP_FILE = "luchess-app.configuration.file";

    /**
     * The key to be used in the web.xml to reference the Luchess Web
     * configuration file: {@value #LUCHESS_WEB_FILE}.
     */
    public static final String LUCHESS_WEB_FILE = "luchess-web.configuration.file";

    private Router router;
    private Presenter presenter;
    private ServicesFactory factory;
    private BuildMonitor monitor;
    private File dataSetRoot;
    private String indexingPassword;

    /**
     * Initializes the context, creating all internal objects.
     * 
     * @param configuration
     *            The configuration passed by the framework, from which one can
     *            retrieve additional configuration files.
     * @throws ClientContextException
     *             When the configuration is missing or not valid.
     */
    public void configure(Properties configuration) throws ClientContextException {
        if (configuration == null) {
            String message = "Configuration must not be null.";
            throw new ClientContextException(message);
        }
        reset();
        configureLuchessWeb(readConfiguration(configuration.getProperty(LUCHESS_WEB_FILE)));
        configureLuchessApp(readConfiguration(configuration.getProperty(LUCHESS_APP_FILE)));
    }

    private void reset() {
        router = null;
        presenter = null;
        factory = null;
        monitor = null;
        dataSetRoot = null;
    }

    private void configureLuchessWeb(Properties config) throws ClientContextException {
        String controllersPath = config.getProperty(WebConfigKeys.PROP_KEY_CONTROLLERS_PATH.getKey());
        String defaultController = config.getProperty(WebConfigKeys.PROP_KEY_CONTROLLERS_DEFAULT.getKey());
        String viewsPath = config.getProperty(WebConfigKeys.PROP_KEY_VIEWS_PATH.getKey());
        String messages = config.getProperty(WebConfigKeys.PROP_KEY_MESSAGES_PATH.getKey());
        String games = config.getProperty(WebConfigKeys.PROP_KEY_GAMES_PATH.getKey());
        URI dataSet = getDataSet(games);
        if (controllersPath == null || defaultController == null || viewsPath == null || messages == null || dataSet == null) {
            String message = "Missing or invalid keys for the configuration of the webapp: controllersPath=%s, defaultController=%s, viewsPath=%s, messages=%s, dataSet=%s";
            message = String.format(message, controllersPath, defaultController, viewsPath, messages, dataSet);
            throw new ClientContextException(message);
        }
        indexingPassword = config.getProperty(WebConfigKeys.PROP_KEY_INDEXING_PASSWORD.getKey());
        router = new PathBasedRouter(controllersPath, defaultController);
        presenter = new FreemarkerPresenter(viewsPath, messages);
        monitor = new BuildMonitor();
        dataSetRoot = Paths.get(dataSet).toFile();
    }

    private void configureLuchessApp(Properties config) throws ClientContextException {
        String servicesFactoryClassName = config.getProperty(AppConfigPropertiesKeys.PROP_KEY_FACTORY.getKey());
        if (servicesFactoryClassName == null) {
            String message = "The services factory has not been specified.";
            throw new ClientContextException(message);
        }
        try {
            factory = (ServicesFactory) Class.forName(servicesFactoryClassName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            String message = "Cannot create ServicesFactory %s.";
            message = String.format(message, servicesFactoryClassName);
            throw new ClientContextException(message, e);
        }
        factory.initialize(config);
    }

    private Properties readConfiguration(String resource) throws ClientContextException {
        Properties configuration = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                String message = "Resource does not exist: %s";
                message = String.format(message, resource);
                throw new ClientContextException(message);
            }
            configuration.load(inputStream);
        } catch (IOException e) {
            String message = "Cannot load properties for %s.";
            message = String.format(message, resource);
            throw new ClientContextException(message, e);
        }
        return configuration;
    }

    private URI getDataSet(String path) throws ClientContextException {
        try {
            URL url = this.getClass().getClassLoader().getResource(path == null ? "" : path);
            if (url != null) {
                return url.toURI();
            }
            File externalDirectory = new File(path);
            if (externalDirectory.exists() && externalDirectory.isDirectory()) {
                return externalDirectory.toURI();
            }
            return null;
        } catch (URISyntaxException e) {
            String message = "Cannot convert to URI.";
            throw new ClientContextException(message, e);
        }
    }

    // --- API ----------------------------------------------------------------

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    /**
     * @return The services factory, ready to use.
     */
    public ServicesFactory getServicesFactory() {
        return factory;
    }

    /**
     * @return The monitor used by the application.
     */
    public BuildMonitor getMonitor() {
        return monitor;
    }

    /**
     * @return a data set for our sample games.
     */
    public SourceDataSet getDataSet() {
        return new FileSourceDataSet(dataSetRoot, new PGNFileSelector(), false);
    }

    /**
     * @return True if the indexing process is protected by a password.
     */
    public boolean hasIndexingPassword() {
        return indexingPassword != null && !indexingPassword.trim().isEmpty();
    }

    /**
     * @param candidate
     *            The indexing password to be tested.
     * @return True if the password is valid.
     */
    public boolean indexingPasswordMatches(String candidate) {
        return indexingPassword != null && indexingPassword.equals(candidate);
    }
}
