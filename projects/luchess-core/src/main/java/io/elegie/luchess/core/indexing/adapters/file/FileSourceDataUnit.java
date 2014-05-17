package io.elegie.luchess.core.indexing.adapters.file;

import io.elegie.luchess.core.api.build.SourceDataUnit;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.indexing.workflow.GameBuilder;
import io.elegie.luchess.pgn.ParserFactory;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SourceDataUnit} implementation based on a file. Contents is first
 * read from the file, then the file extension may be changed, so that processed
 * file are not taken twice, and non-processed files can be corrected.
 */
public class FileSourceDataUnit implements SourceDataUnit {

    /**
     * New extension for processed files: {@value #FILE_PROCESSED}
     */
    public static final String FILE_PROCESSED = ".processed";

    /**
     * New extension for for non-processed files (failure): {@value #FILE_ERROR}
     */
    public static final String FILE_ERROR = ".error";

    private static final Logger LOG = LoggerFactory.getLogger(FileSourceDataUnit.class);

    private File file;
    private Reader reader;
    private boolean renameAfterProcessing;
    private Parser parser = ParserFactory.createSimpleGameParser();

    /**
     * @param file
     *            The file from which data should be read.
     * @param renameAfterProcessing
     *            Should the file extensions be changed after the processing?
     */
    public FileSourceDataUnit(File file, boolean renameAfterProcessing) {
        this.file = file;
        this.renameAfterProcessing = renameAfterProcessing;
    }

    @Override
    public Game next() throws IOException {
        GameBuilder builder = new GameBuilder();
        try {
            parser.digest(reader, builder);
        } catch (ParseException e) {
            String message = "Cannot extract game from following game builder: %s";
            message = String.format(message, builder);
            throw new IOException(message, e);
        }
        return builder.isComplete() ? builder.getGame() : null;
    }

    @Override
    public void beforeProcessing() throws IOException {
        openReader();
    }

    @Override
    public void afterProcessing(boolean success) throws IOException {
        closeReader();
        renameFiles(success);
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {file: %s}";
        return String.format(value, className, file);
    }

    /**
     * @return The file contained within the unit.
     */
    public File getFile() {
        return file;
    }

    // --- Manage reader ------------------------------------------------------

    private void openReader() throws IOException {
        String charset = new FileEncodingSniffer().analyze(file);
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    private void closeReader() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    // --- Rename files -------------------------------------------------------

    private void renameFiles(boolean success) {
        if (renameAfterProcessing) {
            if (success) {
                rename(FILE_PROCESSED);
            } else {
                rename(FILE_ERROR);
            }
        }
    }

    /**
     * @param extension
     *            The extension to append to the name of the source file.
     */
    private void rename(String extension) {
        Path path = file.toPath();
        try {
            Files.move(path, path.resolveSibling(file.getAbsoluteFile() + extension));
        } catch (IOException e) {
            String message = "Cannot rename file.";
            LOG.warn(message, e);
        }
    }

}
