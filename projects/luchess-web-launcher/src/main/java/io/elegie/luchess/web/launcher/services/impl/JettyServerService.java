package io.elegie.luchess.web.launcher.services.impl;

import io.elegie.luchess.web.launcher.services.api.ServerException;
import io.elegie.luchess.web.launcher.services.api.ServerService;
import io.elegie.luchess.web.launcher.services.api.WebApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the server service, backed by a Jetty 9 web server.
 */
public class JettyServerService implements ServerService {

    /**
     * Configuration file for the Jetty server. This file must be located in the
     * classpath, and defaults to: {@value #CONFIGURATION_FILE_NAME}.
     */
    public static final String CONFIGURATION_FILE_NAME = "/configuration.properties";

    /**
     * Property used to specify the server port: {@value #CONFIGURATION_PORT}.
     */
    public static final String CONFIGURATION_PORT = "port";

    /**
     * Default port: {@value #DEFAULT_SERVER_PORT}.
     */
    public static final int DEFAULT_SERVER_PORT = 9001;

    private static final Logger LOG = LoggerFactory.getLogger(JettyServerService.class);
    private static final String USE_SERVER_CLASSLOADER_SLF4J = "org.slf4j.";
    private static final String USE_SERVER_CLASSLOADER_LOGBACK = "ch.qos.logback.";

    private Server server;

    @SuppressWarnings("javadoc")
    public JettyServerService() {
        this.server = new Server(getPortFromConfiguration());
    }

    @Override
    public void start(WebApp webApp) throws ServerException {
        if (webApp == null) {
            String message = "You must specify a web app to start the server.";
            throw new IllegalArgumentException(message);
        }
        if (server.isStopped()) {
            LOG.info("Starting server.");
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath(webApp.getContextPath());
            webapp.setWar(webApp.getWarPath());
            webapp.addSystemClass(USE_SERVER_CLASSLOADER_SLF4J);
            webapp.addSystemClass(USE_SERVER_CLASSLOADER_LOGBACK);
            server.setHandler(webapp);
            try {
                server.start();
            } catch (Exception e) {
                throw new ServerException(e);
            }

            int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
            String url = "http://localhost:" + port + "/";
            LOG.info("Server started (" + url + ").");
        }
    }

    @Override
    public void stop() throws ServerException {
        if (server.isStarted()) {
            LOG.info("Stopping server.");
            try {
                server.stop();
                server.join();
            } catch (Exception e) {
                throw new ServerException(e);
            }
            LOG.info("Server stopped.");
        }
    }

    @Override
    public boolean isStarted() {
        return server.isStarted();
    }

    @Override
    public boolean isStopped() {
        return server.isStopped();
    }

    private int getPortFromConfiguration() {
        Properties configuration = new Properties();
        try (InputStream is = this.getClass().getResourceAsStream(CONFIGURATION_FILE_NAME)) {
            if (is == null) {
                String message = "Cannot find configuration file: %s";
                message = String.format(message, CONFIGURATION_FILE_NAME);
                throw new IOException(message);
            }
            configuration.load(is);
        } catch (IOException e) {
            String message = "Cannot read configuration file (%s). Defaulting to port %s.";
            message = String.format(message, e.getMessage(), DEFAULT_SERVER_PORT);
            LOG.warn(message, e);
            return DEFAULT_SERVER_PORT;
        }

        String value = configuration.getProperty(CONFIGURATION_PORT);
        if (value == null) {
            String message = "Missing port property from configuration file. Defaulting to port %s.";
            message = String.format(message, DEFAULT_SERVER_PORT);
            LOG.warn(message);
            return DEFAULT_SERVER_PORT;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message = "Cannot read port from configuration file (%s). Defaulting to port %s.";
            message = String.format(message, value, DEFAULT_SERVER_PORT);
            LOG.warn(message);
            return DEFAULT_SERVER_PORT;
        }
    }
}
