package io.elegie.luchess.core.indexing.adapters.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

/**
 * This class uses the Mozilla Char Detector, in order to determine which
 * charset is used for a given file.
 */
public class FileEncodingSniffer {

    private static final int BUFFER_SIZE = 1024;
    private static final String DEFAULT_ENCODING = "UTF8";

    private static class CharsetDetectionObserver implements nsICharsetDetectionObserver {
        private String detectedCharset;

        @Override
        public void Notify(String charset) {
            detectedCharset = charset;
        }

        public String getDetectedCharset() {
            return detectedCharset;
        }
    }

    /**
     * @param target
     *            The file for which we are determining the charset.
     * @return The name of the charset.
     * @throws IOException
     *             When the file cannot be read.
     */
    public String analyze(File target) throws IOException {
        nsDetector detector = new nsDetector(nsPSMDetector.ALL);
        CharsetDetectionObserver observer = new CharsetDetectionObserver();
        detector.Init(observer);

        boolean isAscii = true;
        try (FileInputStream in = new FileInputStream(target); BufferedInputStream inputStream = new BufferedInputStream(in)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            boolean done = false;
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                if (isAscii) {
                    isAscii = detector.isAscii(buffer, len);
                }
                if (!isAscii && !done) {
                    done = detector.DoIt(buffer, len, false);
                }
            }
            detector.DataEnd();
        }

        String result = DEFAULT_ENCODING;
        if (isAscii) {
            result = "ASCII";
        } else {
            String detectedCharset = observer.getDetectedCharset();
            if (detectedCharset != null && !detectedCharset.isEmpty()) {
                result = detectedCharset;
            }
        }
        return result;
    }
}
