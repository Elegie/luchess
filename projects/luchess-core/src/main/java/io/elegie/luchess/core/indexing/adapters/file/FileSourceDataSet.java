package io.elegie.luchess.core.indexing.adapters.file;

import io.elegie.luchess.core.api.build.SourceDataUnit;
import io.elegie.luchess.core.indexing.adapters.AbstractSourceDataSet;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link io.elegie.luchess.core.api.build.SourceDataSet} implementation for
 * the file system. The set of data to be indexed will be populated from files
 * contained within a top directory, possibly filtered with a
 * {@link FileSelector}.
 */
public class FileSourceDataSet extends AbstractSourceDataSet {

    private static final Logger LOG = LoggerFactory.getLogger(FileSourceDataSet.class);

    private boolean renameAfterProcessing;

    /**
     * @param source
     *            The top directory.
     * @param selector
     *            The selector to be used to filter out files.
     * @param renameAfterProcessing
     *            Should the processed files be renamed?
     */
    public FileSourceDataSet(File source, FileSelector selector, boolean renameAfterProcessing) {
        this.renameAfterProcessing = renameAfterProcessing;
        collectFiles(source, selector);
    }

    private void collectFiles(File source, FileSelector selector) {
        List<SourceDataUnit> dataUnits = new LinkedList<>();
        collectFiles(source, selector, dataUnits);
        setDataUnitsIterator(dataUnits.iterator());
    }

    private void collectFiles(File source, FileSelector selector, List<SourceDataUnit> dataUnits) {
        if (!source.canRead()) {
            String message = "Source does not exist or cannot be read. Ignoring %s";
            message = String.format(message, source.getPath());
            LOG.warn(message);
        }
        if (source.isFile()) {
            if (selector == null || selector.accept(source)) {
                dataUnits.add(new FileSourceDataUnit(source, renameAfterProcessing));
            } else {
                String message = "File not accepted by the selector. Ignoring %s";
                message = String.format(message, source.getPath());
                LOG.warn(message);
            }
        } else if (source.isDirectory()) {
            File[] files = source.listFiles();
            for (File member : files) {
                collectFiles(member, selector, dataUnits);
            }
        }
    }

}
