package io.elegie.luchess.app.lucene4x.index.manager;

import io.elegie.luchess.app.lucene4x.index.analysis.MoveTextAnalyzer;
import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;
import io.elegie.luchess.core.domain.entities.MoveText;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods to manage the index. Subclasses should provide their own
 * implementations of the underlying {@link Directory} of the index.
 */
public abstract class IndexManager {

    /**
     * Version of the underlying Lucene implementation.
     */
    public static final Version VERSION = Version.LUCENE_47;

    private static final Logger LOG = LoggerFactory.getLogger(IndexManager.class);
    private static final int MAX_RETRIES = 20;
    private static final int WAIT_RETRIES = 200;

    private int analysisDepth;
    private boolean openModeCreate;
    private Analyzer analyzer;
    private Directory directory;
    private IndexWriter writer;
    private IndexReader reader;
    private AtomicLong writerClients = new AtomicLong(0);
    private AtomicLong readerClients = new AtomicLong(0);

    /**
     * @param analysisDepth
     *            The opening moves depth.
     * @param openModeCreate
     *            Whether the index should be open in CREATE mode (i.e. erasing
     *            all previous games, adding all games in a go).
     */
    public IndexManager(int analysisDepth, boolean openModeCreate) {
        this.analysisDepth = analysisDepth;
        this.openModeCreate = openModeCreate;
    }

    /**
     * @return An implementation of a Directory for the index.
     */
    protected abstract Directory createDirectory() throws IOException;

    @SuppressWarnings("javadoc")
    public int getAnalysisDepth() {
        return analysisDepth;
    }

    @SuppressWarnings("javadoc")
    public boolean getOpenModeCreate() {
        return openModeCreate;
    }

    // --- Analyzer -----------------------------------------------------------

    /**
     * The analyzer is reused throughout the lifetime of the application, as it
     * can never change once created.
     * 
     * @return The analyzer used to analyze games.
     */
    @SuppressWarnings("resource")
    synchronized Analyzer getAnalyzer() {
        if (analyzer == null) {
            Map<String, Analyzer> analyzerPerField = new HashMap<>();
            Analyzer defaultAnalyzer = new KeywordAnalyzer();
            analyzerPerField.put(FieldMapper.WHITE.toString(), new SimpleAnalyzer(VERSION));
            analyzerPerField.put(FieldMapper.BLACK.toString(), new SimpleAnalyzer(VERSION));
            analyzerPerField.put(FieldMapper.MOVE_TEXT.toString(), new MoveTextAnalyzer(analysisDepth, MoveText.SEPARATOR));
            analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, analyzerPerField);
        }
        return analyzer;
    }

    // --- Directory ----------------------------------------------------------

    /**
     * The analyzer is reused throughout the lifetime of the application, as it
     * can never change once created.
     * 
     * @return The directory when the index is stored.
     * @throws IOException
     *             When the directory cannot be accessed.
     */
    private synchronized Directory getDirectory() throws IOException {
        if (directory == null) {
            directory = createDirectory();
        }
        return directory;
    }

    // --- IndexWriter --------------------------------------------------------

    /**
     * This method is called each time a service needs a writer. It lets us
     * track the number of concurrent services using the writer, so that we do
     * not inadvertently close in a certain thread a writer being used in
     * another thread.
     * 
     * @return The current index writer to the index.
     * @throws IOException
     *             When the index cannot be written.
     */
    synchronized IndexWriter borrowWriter() throws IOException {
        if (writer == null) {
            @SuppressWarnings("resource")
            Directory target = getDirectory();
            OpenMode mode = openModeCreate ? OpenMode.CREATE : OpenMode.CREATE_OR_APPEND;
            IndexWriterConfig config = new IndexWriterConfig(VERSION, getAnalyzer());
            config.setOpenMode(mode);
            writer = new IndexWriter(target, config);
        }
        writerClients.incrementAndGet();
        return writer;
    }

    /**
     * This method is called each time a service has finished working with a
     * writer.
     */
    void giveBackWriter() {
        writerClients.decrementAndGet();
    }

    private void closeWriter() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                String message = "Cannot close IndexWriter.";
                LOG.error(message, e);
            }
        }
    }

    // --- IndexReader --------------------------------------------------------

    synchronized IndexReader borrowReader() throws IOException {
        if (reader == null) {
            reader = DirectoryReader.open(getDirectory());
        }
        readerClients.incrementAndGet();
        return reader;
    }

    void giveBackReader() {
        readerClients.decrementAndGet();
    }

    private void closeReader() {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                String message = "Cannot close IndexReader.";
                LOG.error(message, e);
            }
        }
    }

    // --- Cleaning up reader / writer ----------------------------------------

    /**
     * <p>
     * This method attempts to close any opened writer / reader. It can only
     * close them though, if they are not in use anymore. If they are, the
     * method waits a bit while holding the lock (so that other threads cannot
     * acquire it in the meantime), until all clients have finished working.
     * </p>
     * 
     * <p>
     * As a consequence, it should be noted that some requests might experience
     * delays if they happen to be made during a clean up process. This is the
     * desired behavior, as the alternative - not sleeping with the lock, but
     * waiting with no lock held for no clients to use any writer / reader -
     * might never be realized, thus letting clients work with an outdated index
     * forever.
     * </p>
     * 
     * <p>
     * It is also possible, though improbable (because of the fast execution
     * speed of most queries), that the clean up method fails if the clients
     * working with a writer / reader keep them too long.
     * </p>
     * 
     * @throws IOException
     */
    synchronized void cleanUp() throws IOException {
        int numRetries = 0;
        while (numRetries < MAX_RETRIES && (readerClients.get() > 0 || writerClients.get() > 0)) {
            numRetries++;
            try {
                Thread.sleep(WAIT_RETRIES);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        }
        boolean cleanUpSuccess = true;
        if (readerClients.get() == 0) {
            closeReader();
        } else {
            String message = "Cannot close index reader, because it is currently in use.";
            LOG.error(message);
            cleanUpSuccess = false;
        }
        if (writerClients.get() == 0) {
            borrowWriter().deleteUnusedFiles();
            giveBackWriter();
            closeWriter();
        } else {
            String message = "Cannot close index writer, because it is currently in use.";
            LOG.error(message);
            cleanUpSuccess = false;
        }
        if (!cleanUpSuccess) {
            String message = "Could not clean up all index resources.";
            throw new IOException(message);
        }
    }
}
