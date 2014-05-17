package io.elegie.luchess.core.indexing.workflow.helpers;

import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple implementation of an {@link IndexMonitor}, which lets us check which
 * lifecycle methods have been called.
 */
@SuppressWarnings("javadoc")
public class IndexMonitorHelper implements IndexMonitor {

    private boolean isStarted;
    private boolean isFinished;
    private boolean isFailed;
    private AtomicInteger gamesCount = new AtomicInteger(0);

    @Override
    public void started() {
        isStarted = true;
    }

    @Override
    public void finished() {
        isFinished = true;
    }

    @Override
    public void failed(BuildException exception) {
        isFailed = true;
    }

    @Override
    public void gameAdded() {
        gamesCount.incrementAndGet();
    }

    public boolean getStarted() {
        return isStarted;
    }

    public boolean getFinished() {
        return isFinished;
    }

    public boolean getFailed() {
        return isFailed;
    }

    public int getGamesCount() {
        return gamesCount.get();
    }
}
