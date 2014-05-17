package io.elegie.luchess.web.launcher.ui.console.helpers;

import io.elegie.luchess.web.launcher.services.api.ServerException;
import io.elegie.luchess.web.launcher.services.api.ServerService;
import io.elegie.luchess.web.launcher.services.api.WebApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple service used to test the console.
 */
@SuppressWarnings("javadoc")
public class ServerServiceHelper implements ServerService {

    public static final String STARTED = "started";
    public static final String STOPPED = "stopped";

    private static final Logger LOG = LoggerFactory.getLogger(ServerServiceHelper.class);

    private boolean started = false;
    private boolean stopped = true;
    private boolean exceptionOnStart;
    private boolean exceptionOnStop;

    @Override
    public void start(WebApp webApp) throws ServerException {
        if (exceptionOnStart) {
            throw new ServerException("ExceptionOnStart");
        }
        started = true;
        stopped = false;
        LOG.info(STARTED + " " + WebAppServiceHelper.CONTEXT_PATH);
    }

    @Override
    public void stop() throws ServerException {
        if (exceptionOnStop) {
            throw new ServerException("ExceptionOnStop");
        }
        started = false;
        stopped = true;
        LOG.info(STOPPED);
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    public void setExceptionOnStart(boolean exceptionOnStart) {
        this.exceptionOnStart = exceptionOnStart;
    }

    public void setExceptionOnStop(boolean exceptionOnStop) {
        this.exceptionOnStop = exceptionOnStop;
    }

}
