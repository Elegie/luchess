package io.elegie.luchess.app.lucene4x.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;

/**
 * An analyzer which should analyze sequences of moves. It merely wraps a
 * {@link MoveTextTokenizer}, which holds all the analysis logic.
 */
public class MoveTextAnalyzer extends Analyzer {

    private int analysisDepth;
    private char moveSeparator;

    /**
     * @param analysisDepth
     *            The maximum opening depth analysis.
     * @param moveSeparator
     *            The character used to separate moves in the move text.
     */
    public MoveTextAnalyzer(int analysisDepth, char moveSeparator) {
        this.analysisDepth = analysisDepth;
        this.moveSeparator = moveSeparator;
    }

    @SuppressWarnings("resource")
    @Override
    protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
        return new TokenStreamComponents(new MoveTextTokenizer(reader, analysisDepth, moveSeparator));
    }

}
