package io.elegie.luchess.core.indexing.adapters.file;

import java.io.File;

/**
 * A selector that accepts only PGN files.
 */
public class PGNFileSelector implements FileSelector {

    /**
     * PGN files must end with this extension: {@value #EXTENSION_PGN}
     */
    public static final String EXTENSION_PGN = ".pgn";

    @Override
    public boolean accept(File file) {
        return file.getAbsolutePath().endsWith(EXTENSION_PGN);
    }

}
