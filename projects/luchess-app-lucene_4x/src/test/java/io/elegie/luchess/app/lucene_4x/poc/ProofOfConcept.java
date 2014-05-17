package io.elegie.luchess.app.lucene_4x.poc;

import static org.junit.Assert.assertEquals;
import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.payloads.AveragePayloadFunction;
import org.apache.lucene.search.payloads.PayloadTermQuery;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * Proof of concept of how to store and retrieve data from the index.
 */
public class ProofOfConcept {

    private static final Version VERSION = Version.LUCENE_47;
    private static final int NUM_GAMES = 2;
    private static final String FIELD_NAME = FieldMapper.MOVE_TEXT.toString();

    /**
     * Games are indexed as documents, with fields for players, elos, result and
     * gametext. The gametext contains many tokens, all made of moves separated
     * by a space. The main token is the full game, against which one can
     * perform PrefixQueries. Other tokens contain only the starting moves
     * ("e4", "e4 e5", "e4 e5 d4" and so forth), adding one move until the
     * analysis depth has been reached. Each of these "opening token" has a
     * payload, which contains the next move, so that we can calculate
     * continuations, using specific collector.
     * 
     * @throws IOException
     */
    @Test
    public void testIndexWithPayloads() throws IOException {
        Directory directory = new RAMDirectory();
        @SuppressWarnings("resource")
        IndexWriter writer = createWriter(directory);
        indexGames(writer);
        closeWriter(writer);

        IndexReader reader = DirectoryReader.open(directory);
        matchAllDocs(reader);
        matchE4Docs(reader);
        matchFullGame(reader);
        reader.close();
        directory.close();
    }

    @SuppressWarnings("resource")
    private IndexWriter createWriter(Directory directory) throws IOException {
        Analyzer analyzer = createAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(VERSION, analyzer);
        config.setOpenMode(OpenMode.CREATE);
        return new IndexWriter(directory, config);
    }

    private void closeWriter(IndexWriter writer) throws IOException {
        writer.getAnalyzer().close();
        writer.close();
    }

    @SuppressWarnings("resource")
    private Analyzer createAnalyzer() {
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        Analyzer defaultAnalyzer = new KeywordAnalyzer();
        analyzerPerField.put(FIELD_NAME, new MoveTextAnalyzer());
        return new PerFieldAnalyzerWrapper(defaultAnalyzer, analyzerPerField);
    }

    private void indexGames(IndexWriter writer) throws IOException {
        Document document1 = new Document();
        String value1 = "e4 e5 Nc3";
        Field field1 = new TextField(FIELD_NAME, value1, Field.Store.YES);
        document1.add(field1);
        writer.addDocument(document1);

        Document document2 = new Document();
        String value2 = "e4 Nf6 Nf3";
        Field field2 = new TextField(FIELD_NAME, value2, Field.Store.YES);
        document2.add(field2);
        writer.addDocument(document2);
    }

    private void matchAllDocs(IndexReader reader) throws IOException {
        MatchAllDocsQuery query = new MatchAllDocsQuery();
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs results = searcher.search(query, NUM_GAMES);
        assertEquals(NUM_GAMES, results.scoreDocs.length);
    }

    private void matchE4Docs(IndexReader reader) throws IOException {
        matchSequence(reader, "e4", 2, 2);
    }

    private void matchFullGame(IndexReader reader) throws IOException {
        matchSequence(reader, "e4 e5 Nc3", 1, 0);
    }

    private void matchSequence(IndexReader reader, String sequence, int docs, int continuations) throws IOException {
        ContinuationCollector collector = new ContinuationCollector();
        ContinuationSimilarity similarity = new ContinuationSimilarity(collector);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(similarity);

        Query query = new PayloadTermQuery(new Term(FIELD_NAME, sequence), new AveragePayloadFunction());
        searcher.search(query, collector);
        TopDocs results = collector.getTopDocs();
        assertEquals(docs, results.scoreDocs.length);
        assertEquals(continuations, collector.getContinuationsCount());
    }

    // --- Analysis -----------------------------------------------------------

    private class MoveTextAnalyzer extends Analyzer {
        @SuppressWarnings("resource")
        @Override
        protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
            return new TokenStreamComponents(new FullyAnalyzedMoveTextTokenizer(reader));
        }
    }

    private class FullyAnalyzedMoveTextTokenizer extends Tokenizer {
        private static final int MAX_MOVE_LENGTH = 7;

        private List<Byte> previousMove;
        private List<List<Byte>> allMoves;
        private boolean finished;

        private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
        private PositionIncrementAttribute positionAtt = addAttribute(PositionIncrementAttribute.class);
        private PayloadAttribute payloadAtt = addAttribute(PayloadAttribute.class);

        protected FullyAnalyzedMoveTextTokenizer(Reader input) {
            super(input);
        }

        @Override
        public final void reset() throws IOException {
            super.reset();
            allMoves = new ArrayList<>();
            previousMove = null;
            finished = false;
        }

        @Override
        public final boolean incrementToken() throws IOException {
            if (finished) {
                return false;
            }
            initialize();
            return process();
        }

        private void initialize() throws IOException {
            clearAttributes();
            if (previousMove == null) {
                previousMove = readNextMove();
                if (previousMove.isEmpty()) {
                    String message = "No move found in the current stream.";
                    throw new IOException(message);
                }
            } else {
                positionAtt.setPositionIncrement(0);
            }
        }

        private boolean process() throws IOException {
            allMoves.add(previousMove);
            termAtt.append(new String(getAllMoves(), "UTF-8"));
            List<Byte> currentMove = readNextMove();
            if (currentMove.isEmpty()) {
                finished = true;
            } else {
                payloadAtt.setPayload(new BytesRef(getBytes(currentMove)));
                previousMove = currentMove;
            }
            return true;
        }

        private List<Byte> readNextMove() throws IOException {
            List<Byte> result = new ArrayList<>(MAX_MOVE_LENGTH);
            int data = input.read();
            while (data != ' ' && data != -1) {
                result.add((byte) data);
                data = input.read();
            }
            return result;
        }

        private byte[] getAllMoves() {
            List<Byte> result = new ArrayList<>();
            int size = allMoves.size();
            for (int ii = 0; ii < size; ii++) {
                result.addAll(allMoves.get(ii));
                if (ii < size - 1) {
                    result.add((byte) ' ');
                }
            }
            return getBytes(result);
        }

        private byte[] getBytes(List<Byte> source) {
            byte[] target = new byte[source.size()];
            for (int j = 0; j < source.size(); j++) {
                target[j] = source.get(j);
            }
            return target;
        }
    }

    // --- Search -------------------------------------------------------------

    private class ContinuationCollector extends Collector {
        private TopScoreDocCollector topScoreDocCollector;
        private int continuationsCount;

        public ContinuationCollector() {
            topScoreDocCollector = TopScoreDocCollector.create(NUM_GAMES, true);
        }

        @Override
        public void setScorer(Scorer scorer) throws IOException {
            topScoreDocCollector.setScorer(scorer);
        }

        @Override
        public void collect(int doc) throws IOException {
            topScoreDocCollector.collect(doc);
        }

        @Override
        public void setNextReader(AtomicReaderContext context) throws IOException {
            topScoreDocCollector.setNextReader(context);
        }

        @Override
        public boolean acceptsDocsOutOfOrder() {
            return topScoreDocCollector.acceptsDocsOutOfOrder();
        }

        public TopDocs getTopDocs() {
            return topScoreDocCollector.topDocs(0, NUM_GAMES);
        }

        public void setPayload(@SuppressWarnings("unused") BytesRef payload) {
            continuationsCount++;
        }

        public int getContinuationsCount() {
            return continuationsCount;
        }
    }

    private class ContinuationSimilarity extends DefaultSimilarity {
        private ContinuationCollector collector;

        public ContinuationSimilarity(ContinuationCollector collector) {
            this.collector = collector;
        }

        @Override
        public float scorePayload(int doc, int start, int end, BytesRef payload) {
            collector.setPayload(payload);
            return super.scorePayload(doc, start, end, payload);
        }
    }

}
