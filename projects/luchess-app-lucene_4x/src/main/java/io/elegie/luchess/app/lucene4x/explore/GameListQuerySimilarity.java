package io.elegie.luchess.app.lucene4x.explore;

import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;

/**
 * The default similarity, extended so as to provide the payload to the
 * collector. The payload should contain the continuation, set during the
 * indexing.
 * 
 * @see io.elegie.luchess.app.lucene4x.index.analysis.MoveTextTokenizer
 */
public class GameListQuerySimilarity extends DefaultSimilarity {
    private GameListQueryCollector collector;

    /**
     * @param collector
     *            The collector to notify the payload to.
     */
    public GameListQuerySimilarity(GameListQueryCollector collector) {
        this.collector = collector;
    }

    /**
     * <p>
     * We pass the payload to the collector in this method.
     * </p>
     * 
     * <p>
     * Note 1: this method may be called many times with different documents, so
     * the collector will need to retain the right payload, matching with the
     * docID passed to its {@link GameListQueryCollector#collect(int docID)}.
     * </p>
     * 
     * <p>
     * Note 2: the BytesRef object passed as parameter is actually reused by
     * Lucene under the hood, so we need to deep-clone it, in order to provide
     * the collector with an immutable value.
     * </p>
     */
    @Override
    public float scorePayload(int doc, int start, int end, BytesRef payload) {
        collector.submitContinuation(doc, BytesRef.deepCopyOf(payload));
        return super.scorePayload(doc, start, end, payload);
    }
}
