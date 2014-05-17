package io.elegie.luchess.web.client.models;

import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This object holds information related to the last / current build. It acts as
 * an execution report, and as such can be stored, displayed, e-mailed, or
 * whatever a controller sees fitting.
 */
public class BuildMonitor implements IndexMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(IndexMonitor.class);

    /**
     * Possible statuses of the monitor.
     */
    @SuppressWarnings("javadoc")
    public static enum Status {
        CREATED("created"),
        STARTED("started"),
        FINISHED("finished"),
        FAILED("failed");

        private String value;

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The current status of the monitor.
     */
    private long buildStart;
    private long buildEnd;
    private Status status = Status.CREATED;
    private final AtomicLong numGames = new AtomicLong(0);
    private final AtomicLong numFailures = new AtomicLong(0);

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {numGames: %s, start: %s, end: %s, status: %s}";
        return String.format(value, className, numGames, buildStart, buildEnd, status);
    }

    // --- Monitor Implementation ---------------------------------------------

    @Override
    public synchronized void started() {
        if (!status.equals(Status.STARTED)) {
            buildStart = System.currentTimeMillis();
            buildEnd = 0;
            status = Status.STARTED;
            numGames.set(0);
            numFailures.set(0);
        } else {
            String message = "Cannot call started() on an already-started visitor.";
            throw new IllegalStateException(message);
        }
    }

    @Override
    public synchronized void finished() {
        if (status.equals(Status.STARTED)) {
            buildEnd = System.currentTimeMillis();
            status = Status.FINISHED;
        } else {
            String message = "Cannot call finished() on a not-started visitor.";
            throw new IllegalStateException(message);
        }
    }

    @Override
    public synchronized void failed(BuildException exception) {
        numFailures.incrementAndGet();
        status = Status.FAILED;
        String message = "Error encountered while indexing.";
        LOG.error(message, exception);
    }

    @Override
    public void gameAdded() {
        numGames.incrementAndGet();
    }

    // --- Getters --------------------------------------------------------

    /**
     * @return The status of the current build.
     */
    public synchronized Status getStatus() {
        return status;
    }

    /**
     * @return The number of games indexed so far.
     */
    public synchronized long getNumGames() {
        return numGames.longValue();
    }

    /**
     * @return The duration the indexing took (has currently taken if running),
     *         zero if not started.
     */
    public synchronized long getDuration() {
        long duration = 0;
        if (buildStart > 0) {
            if (buildEnd == 0) {
                duration = System.currentTimeMillis() - buildStart;
            } else {
                duration = buildEnd - buildStart;
            }
        }
        return duration;
    }

    /**
     * @return True if there was an unacknowledged failure.
     */
    public synchronized boolean acknowledgeFailure() {
        if (numFailures.get() > 0) {
            numFailures.decrementAndGet();
            return true;
        }
        return false;
    }
}