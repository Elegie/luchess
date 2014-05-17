package io.elegie.luchess.core.indexing.adapters.file;

import java.io.File;

/**
 * This interface represents a selection strategy for files, so that a
 * {@link FileSourceDataSet} knows whether to accept a given file, to feed its
 * contents to some
 * {@link io.elegie.luchess.core.indexing.workflow.GameIndexer}.
 */
public interface FileSelector {

    /**
     * @param file
     *            The file to be indexed.
     * @return True if the file should be indexed.
     */
    boolean accept(File file);

}
