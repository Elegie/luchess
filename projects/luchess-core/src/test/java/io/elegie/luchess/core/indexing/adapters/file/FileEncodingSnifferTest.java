package io.elegie.luchess.core.indexing.adapters.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests that we can properly find out the charset used for a given file. We
 * wouldn't like to have players' name with strange characters!
 */
@SuppressWarnings("javadoc")
public class FileEncodingSnifferTest {

    private static final FileEncodingSniffer sniffer = new FileEncodingSniffer();

    @Test
    public void testAscii() throws IOException {
        assertEquals("ASCII", sniffer.analyze(getFile("file.ascii")));
    }

    @Test
    public void testUtf8() throws IOException {
        assertEquals("UTF8", sniffer.analyze(getFile("file.utf8")));
    }

    private File getFile(String resource) {
        return new File(FileEncodingSnifferTest.class.getResource("/" + resource).getFile());
    }

}
