package io.elegie.luchess.core.indexing.adapters.file;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.api.build.SourceDataUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests that all files are properly considered when working with a
 * {@link FileSourceDataSet}, taking into account a {@link PGNFileSelector},
 * checking that files are properly renamed in the end.
 */
public class FileSourceDataSetTest {

    private static final String FILE_PROCESSED = "file-processed";
    private static final String FILE_ERROR = "file-error";
    private static final String FILE_IGNORED = "file-ignored";

    /**
     * Our temporary test folder, gracefully contributed by JUnit.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * We write 3 files: the first should be processed, the second should be in
     * error, and the third one should be ignored.
     * 
     * @throws IOException
     *             When we cannot work with the temporary folder.
     */
    @Test
    public void testFilesTraversal() throws IOException {
        File root = folder.getRoot();
        File subfolder = new File(makePath(root, "subfolder"));
        subfolder.mkdir();

        File processed = new File(makePath(subfolder, FILE_PROCESSED + PGNFileSelector.EXTENSION_PGN));
        File error = new File(makePath(subfolder, FILE_ERROR + PGNFileSelector.EXTENSION_PGN));
        File ignored = new File(makePath(subfolder, FILE_IGNORED));

        writeFile(processed);
        writeFile(error);
        writeFile(ignored);

        FileSourceDataSet dataSet = new FileSourceDataSet(root, new PGNFileSelector(), true);
        SourceDataUnit dataUnit = dataSet.nextUnit();
        while (dataUnit != null) {
            boolean success = !((FileSourceDataUnit) dataUnit).getFile().getName().contains(FILE_ERROR);
            dataUnit.beforeProcessing();
            dataUnit.next();
            dataUnit.afterProcessing(success);
            dataUnit = dataSet.nextUnit();
        }

        List<File> files = Arrays.asList(subfolder.listFiles());
        assertTrue(files.contains(new File(processed.getAbsoluteFile() + FileSourceDataUnit.FILE_PROCESSED)));
        assertTrue(files.contains(new File(error.getAbsoluteFile() + FileSourceDataUnit.FILE_ERROR)));
        assertTrue(files.contains(ignored));
    }

    /**
     * No data unit available when the source directory is wrong.
     */
    @Test
    public void testNullSourceDirectory() {
        FileSourceDataSet dataSet = new FileSourceDataSet(new File("foo"), new PGNFileSelector(), true);
        assertNull(dataSet.nextUnit());
    }

    /**
     * No selector, means everything is accepted.
     * 
     * @throws IOException
     */
    @Test
    public void testNoSelector() throws IOException {
        File root = folder.getRoot();
        writeFile(new File(makePath(root, FILE_PROCESSED)));

        FileSourceDataSet dataSet = new FileSourceDataSet(root, null, true);
        assertNotNull(dataSet.nextUnit());
        assertNull(dataSet.nextUnit());
    }

    private void writeFile(File file) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        output.write("[Result \"1-0\"] 1.e4 1-0");
        output.close();
    }

    private String makePath(File root, String name) {
        return root.getAbsolutePath() + File.separatorChar + name;
    }

}
