package io.elegie.luchess.app.lucene4x.explore;

import org.apache.lucene.search.payloads.AveragePayloadFunction;

/**
 * A payload function that simply returns 1 as payload score.
 */
public class GameListQueryPayloadFunction extends AveragePayloadFunction {

    @Override
    public float currentScore(int docId, String field, int start, int end, int numPayloadsSeen, float currentScore,
            float currentPayloadScore) {
        return currentScore;
    }

    @Override
    public float docScore(int docId, String field, int numPayloadsSeen, float payloadScore) {
        return 1;
    }
}
