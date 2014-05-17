package io.elegie.luchess.core.api.helpers;

import io.elegie.luchess.core.api.BuilderService;
import io.elegie.luchess.core.api.ServicesFactory;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataSet;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility methods to facilitate index writing / reading.
 */
public final class IndexHelper {

    private IndexHelper() {
    }

    /**
     * This method indexes all games contained in a data set, blocking until the
     * indexing has finished or failed.
     * 
     * @param dataSet
     *            Data set containing the games to be indexed.
     * @param factory
     *            The services factory to be used to index the games.
     * @throws BuildException
     *             When the indexing fails.
     * @throws InterruptedException
     *             When the indexing fails.
     */
    public static void indexDataSet(SourceDataSet dataSet, ServicesFactory factory) throws BuildException,
            InterruptedException {
        BuilderService builder = factory.getBuilderService();

        final AtomicBoolean finished = new AtomicBoolean(false);
        final AtomicBoolean failed = new AtomicBoolean(false);
        final StringBuffer errorMessage = new StringBuffer();
        final Object lock = new Object();

        IndexMonitor monitor = new IndexMonitor() {
            @Override
            public void failed(BuildException exception) {
                errorMessage.append(exception.getMessage());
                failed.set(true);
                synchronized (lock) {
                    lock.notifyAll();
                }
            }

            @Override
            public void finished() {
                finished.set(true);
                synchronized (lock) {
                    lock.notifyAll();
                }
            }

            @Override
            public void started() {
            }

            @Override
            public void gameAdded() {
            }
        };

        builder.index(dataSet, monitor);
        synchronized (lock) {
            while (!(finished.get() || failed.get())) {
                lock.wait();
            }
        }

        if (failed.get()) {
            throw new BuildException(errorMessage.toString());
        }
    }
}
