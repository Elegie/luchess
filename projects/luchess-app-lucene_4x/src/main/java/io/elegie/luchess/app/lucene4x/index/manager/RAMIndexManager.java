package io.elegie.luchess.app.lucene4x.index.manager;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * A manager providing a {@link RAMDirectory} directory.
 */
public class RAMIndexManager extends IndexManager {

    @SuppressWarnings("javadoc")
    public RAMIndexManager(int analysisDepth, boolean openModeCreate) {
        super(analysisDepth, openModeCreate);
    }

    @Override
    protected Directory createDirectory() throws IOException {
        return new RAMDirectory();
    }
}
