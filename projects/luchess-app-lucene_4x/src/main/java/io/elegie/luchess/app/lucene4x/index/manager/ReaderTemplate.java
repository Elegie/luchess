package io.elegie.luchess.app.lucene4x.index.manager;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.Version;

/**
 * This class lets us encapsulate a read in the index, managing the lifecycle of
 * reader accesses. We want to make sure that the reader obtained will not be
 * closed by another thread during the execution of the query.
 * 
 * @param <T>
 *            The type of the value returned by the read method.
 */
public abstract class ReaderTemplate<T> {

    private IndexManager indexManager;

    /**
     * @param indexManager
     *            The instance of the manager from which one can obtain the
     *            index objects (reader, analyzer).
     */
    public ReaderTemplate(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    /**
     * @return The value returned by the doRead method.
     * @throws IOException
     *             When the index cannot be read.
     */
    @SuppressWarnings("resource")
    public final T execute() throws IOException {
        Version version = IndexManager.VERSION;
        IndexReader reader = indexManager.borrowReader();
        Analyzer analyzer = indexManager.getAnalyzer();
        int analysisDepth = indexManager.getAnalysisDepth();
        ReaderTemplateArg arg = new ReaderTemplateArg(version, reader, analyzer, analysisDepth);
        T result = doRead(arg);
        indexManager.giveBackReader();
        return result;
    }

    /**
     * Subclasses should put their domain logic in this method. They should also
     * never alter the lifecycle of the reader and analyzer objects.
     * 
     * @param arg
     * @return The result of the domain processing.
     */
    protected abstract T doRead(ReaderTemplateArg arg) throws IOException;

    // ------------------------------------------------------------------------

    /**
     * Wrapper for reader-related objects.
     */
    @SuppressWarnings("javadoc")
    public static class ReaderTemplateArg {

        private Version version;
        private IndexReader reader;
        private Analyzer analyzer;
        private int analysisDepth;

        public ReaderTemplateArg(Version version, IndexReader reader, Analyzer analyzer, int analysisDepth) {
            this.version = version;
            this.reader = reader;
            this.analyzer = analyzer;
            this.analysisDepth = analysisDepth;
        }

        public Version getVersion() {
            return version;
        }

        public IndexReader getIndexReader() {
            return reader;
        }

        public Analyzer getAnalyzer() {
            return analyzer;
        }

        public int getAnalysisDepth() {
            return analysisDepth;
        }

    }

}
