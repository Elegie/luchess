package io.elegie.luchess.core.indexing.workflow;

import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.api.build.SourceDataUnit;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This is the main entry point of the indexing framework. Implementations are
 * supposed to create an IndexingSession, feed it using the appropriate setters,
 * then run it, possibly in a separate thread.
 * </p>
 * 
 * <p>
 * The session processes all data contained in the {@link SourceDataSet}, using
 * the threading profile defined in the {@link ThreadingProfile}, reporting its
 * progress through the {@link IndexMonitor}, and notifying the
 * {@link GameIndexer} each time a new game is processed from the source.
 * </p>
 * 
 * <p>
 * Note that exceptions thrown in tasks are managed in an overridden lifecycle
 * method of the executor service, and not in the session itself.
 * </p>
 */
@SuppressWarnings("javadoc")
public class IndexingSession implements Runnable {

    private SourceDataSet dataSet;
    private IndexMonitor monitor;
    private GameIndexer indexer;
    private ThreadingProfile profile;

    @Override
    public void run() {
        assertCanRun();
        monitor.started();
        IndexingExecutorService executor = new IndexingExecutorService(profile.getNumberOfThreads());
        SourceDataUnit dataUnit = dataSet.nextUnit();
        while (dataUnit != null) {
            IndexingTask task = new IndexingTask();
            task.setDataUnit(dataUnit);
            task.setMonitor(monitor);
            task.setIndexer(indexer);
            executor.submit(task);
            dataUnit = dataSet.nextUnit();
        }
        executor.shutdown();
        try {
            executor.awaitTermination(profile.getTimeoutSeconds(), TimeUnit.SECONDS);
            if (executor.containsException()) {
                String message = "The executor service of the session has reported an exception.";
                monitor.failed(new BuildException(message));
            }
        } catch (InterruptedException e) {
            String message = "The executor service of the session has reported an interruption.";
            monitor.failed(new BuildException(message, e));
        }
        monitor.finished();
    }

    private void assertCanRun() {
        if (dataSet == null || monitor == null || indexer == null || profile == null) {
            String message = "The build session has not been initialized properly (dataSet=%s, monitor=%s, indexer=%s, profile=%s)";
            message = String.format(message, dataSet, monitor, indexer, profile);
            throw new AssertionError(message);
        }
    }

    public SourceDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(SourceDataSet dataSet) {
        this.dataSet = dataSet;
    }

    public IndexMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IndexMonitor monitor) {
        this.monitor = monitor;
    }

    public GameIndexer getIndexer() {
        return indexer;
    }

    public void setIndexer(GameIndexer indexer) {
        this.indexer = indexer;
    }

    public ThreadingProfile getProfile() {
        return profile;
    }

    public void setProfile(ThreadingProfile profile) {
        this.profile = profile;
    }

}
