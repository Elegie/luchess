package io.elegie.luchess.app.lucene4x.index.manager;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * A manager providing a {@link FSDirectory} directory. The path to the
 * directory should be passed to the constructor.
 */
public class FSIndexManager extends IndexManager {

    private File indexPath;

    @SuppressWarnings("javadoc")
    public FSIndexManager(int analysisDepth, boolean openModeCreate, File indexPath) {
        super(analysisDepth, openModeCreate);
        this.indexPath = indexPath;
    }

    @Override
    protected Directory createDirectory() throws IOException {
        return FSDirectory.open(indexPath);
    }

}
