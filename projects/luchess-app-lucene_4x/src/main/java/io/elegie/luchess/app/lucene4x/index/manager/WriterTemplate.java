package io.elegie.luchess.app.lucene4x.index.manager;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

/**
 * This class lets us encapsulate a write in the index, managing the lifecycle
 * of writer accesses. We want to make sure that the writer obtained will not be
 * closed by another thread during the execution of the build.
 */
public abstract class WriterTemplate {

    private IndexManager indexManager;

    /**
     * @param indexManager
     *            The instance of the manager from which one can obtain the
     *            index objects (reader, analyzer).
     */
    public WriterTemplate(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    /**
     * @throws IOException
     *             When the index cannot be written.
     */
    public final void execute() throws IOException {
        @SuppressWarnings("resource")
        IndexWriter writer = indexManager.borrowWriter();
        doWrite(writer, new CleanUp(indexManager));
    }

    protected abstract void doWrite(IndexWriter writer, CleanUp cleanUp);

    // ------------------------------------------------------------------------

    /**
     * Implementations of the template should make sure that the clean up method
     * be called once they have finished processing their domain logic.
     */
    @SuppressWarnings("javadoc")
    public static class CleanUp {

        private IndexManager indexManager;

        public CleanUp(IndexManager indexManager) {
            this.indexManager = indexManager;
        }

        public void proceed() throws IOException {
            indexManager.giveBackWriter();
            indexManager.cleanUp();
        }

    }

}
