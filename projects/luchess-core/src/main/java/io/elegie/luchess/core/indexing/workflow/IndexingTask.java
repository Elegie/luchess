package io.elegie.luchess.core.indexing.workflow;

import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataUnit;
import io.elegie.luchess.core.domain.entities.Game;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * This task processes all games from a source data unit. It is intended to run
 * in a threaded indexing session, and can gracefully handle interruptions.
 */
@SuppressWarnings("javadoc")
public class IndexingTask implements Callable<Void> {

    private SourceDataUnit dataUnit;
    private IndexMonitor monitor;
    private GameIndexer indexer;

    @Override
    public Void call() throws IOException {
        boolean success = true;
        try {
            dataUnit.beforeProcessing();
            Game game = dataUnit.next();
            while (game != null) {
                indexer.add(game);
                monitor.gameAdded();
                if (Thread.currentThread().isInterrupted()) {
                    String message = "Interrupting %s.";
                    message = String.format(message, dataUnit);
                    throw new IOException(message);
                }
                game = dataUnit.next();
            }
        } catch (IOException e) {
            success = false;
            monitor.failed(new BuildException(e));
        } finally {
            try {
                dataUnit.afterProcessing(success);
            } catch (IOException e) {
                success = false;
                monitor.failed(new BuildException(e));
            }
        }
        if (!success) {
            String message = "Indexing task has failed.";
            throw new IOException(message);
        }
        return null;
    }

    public SourceDataUnit getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(SourceDataUnit dataUnit) {
        this.dataUnit = dataUnit;
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

}
