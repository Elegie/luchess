package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;
import io.elegie.luchess.core.domain.entities.Continuation;
import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.moves.processors.MoveParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.Ints;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This collector wraps a {@link TopScoreDocCollector}, and lets us accumulate
 * data regarding continuations. Continuations were initially stored during
 * indexing, as payload on indexed terms.
 * </p>
 * 
 * <p>
 * This collector is used with a
 * {@link io.elegie.luchess.app.lucene4x.explore.GameListQuerySimilarity}, which
 * gets the payload during the search, and calls the
 * {@link #submitContinuation(int, BytesRef)} method to transmit it.
 * </p>
 */
public class GameListQueryCollector extends Collector {

    private static final Logger LOG = LoggerFactory.getLogger(GameListQueryCollector.class);

    private int pageStart;
    private int pageCount;

    private TopScoreDocCollector topScoreDocCollector;
    private Ints cachedResults;

    private Continuation currentContinuation = new Continuation();
    private Map<BytesRef, Continuation> statistics = new HashMap<>();
    private Map<Integer, BytesRef> submittedContinuations = new HashMap<>();

    protected GameListQueryCollector(int pageStart, int pageCount) {
        this.pageStart = pageStart;
        this.pageCount = pageCount;
        this.topScoreDocCollector = TopScoreDocCollector.create(pageStart * pageCount, true);
    }

    // --- API ----------------------------------------------------------------

    /**
     * @return The top documents, properly paged thanks to the
     *         {@link TopScoreDocCollector}.
     */
    public TopDocs getTopDocs() {
        return topScoreDocCollector.topDocs((pageStart - 1) * pageCount, pageCount);
    }

    /**
     * @return The continuations for the current move. May be empty if no
     *         continuation is available.
     */
    public List<Continuation> getContinuations() {
        return new ArrayList<>(statistics.values());
    }

    /**
     * @return The number of victories by White, for the given search criteria.
     */
    public int getTotalWhiteWins() {
        return currentContinuation.getTotalWhiteWins();
    }

    /**
     * @return The number of victories by Black, for the given search criteria.
     */
    public int getTotalBlackWins() {
        return currentContinuation.getTotalBlackWins();
    }

    /**
     * @return The number of draws, for the given search criteria.
     */
    public int getTotalDraws() {
        return currentContinuation.getTotalDraws();
    }

    /**
     * @return The number of unfinished games, for the given criteria.
     */
    public int getTotalUnfinished() {
        return currentContinuation.getTotalUnfinished();
    }

    // --- Collector Implementation -------------------------------------------

    @Override
    public void setScorer(Scorer scorer) throws IOException {
        topScoreDocCollector.setScorer(scorer);
    }

    @Override
    public void collect(int docID) throws IOException {
        topScoreDocCollector.collect(docID);
        collectResult(docID, currentContinuation);
        collectContinuation(docID);
    }

    @Override
    public void setNextReader(AtomicReaderContext atomicReaderContext) throws IOException {
        topScoreDocCollector.setNextReader(atomicReaderContext);
        cachedResults = FieldCache.DEFAULT.getInts(atomicReaderContext.reader(), FieldMapper.RESULT.toString(), false);
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return topScoreDocCollector.acceptsDocsOutOfOrder();
    }

    /**
     * This method is called by the similarity when a payload has been found. We
     * simply save the payload along with the submitted docID, so that we can
     * retrieve it when the collector starts its collection.
     * 
     * @param docID
     *            The ID of the document, in the current reader's context.
     * @param continuation
     *            The continuation.
     */
    public void submitContinuation(int docID, BytesRef continuation) {
        submittedContinuations.put(docID, continuation);
    }

    // --- Helpers ------------------------------------------------------------

    /**
     * This method looks up a continuation in the submitted continuations,
     * matching the current document ID, and aggregates the related statistics
     * if available.
     * 
     * @param docID
     *            The ID of the current document.
     */
    private void collectContinuation(int docID) {
        BytesRef payload = submittedContinuations.get(docID);
        if (payload != null) {
            Continuation continuation;
            if (statistics.containsKey(payload)) {
                continuation = statistics.get(payload);
            } else {
                continuation = new Continuation();
                String moveValue = payload.utf8ToString();
                Move move = MoveParser.convert(moveValue);
                if (move != null) {
                    continuation.setMove(move);
                } else {
                    String message = "Cannot convert the continuation move (%s).";
                    message = String.format(message, moveValue);
                    LOG.error(message);
                }
                statistics.put(payload, continuation);
            }
            collectResult(docID, continuation);
        }
        submittedContinuations.clear();
    }

    private void collectResult(int docID, Continuation continuation) {
        int bitResult = cachedResults.get(docID);
        if (bitResult == 1) {
            continuation.incrementWhiteWins();
        } else if (bitResult == -1) {
            continuation.incrementBlackWins();
        } else if (bitResult == 0) {
            continuation.incrementDraws();
        } else {
            continuation.incrementUnfinished();
        }
    }

}
