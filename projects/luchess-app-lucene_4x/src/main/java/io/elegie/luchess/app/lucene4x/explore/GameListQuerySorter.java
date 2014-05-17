package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;

import java.io.IOException;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.Ints;
import org.apache.lucene.search.Query;

/**
 * This sorter makes sure that games having high elo are boosted, as they might
 * be more interesting to the clients.
 */
public class GameListQuerySorter extends CustomScoreQuery {

    @SuppressWarnings("javadoc")
    public GameListQuerySorter(Query source) {
        super(source);
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
        return new EloWinRateBooster(context);
    }

    // --- Booster Implementation ---------------------------------------------

    private static class EloWinRateBooster extends CustomScoreProvider {

        private static final float ELO_GRAND_MASTER = 2500;
        private static final float ELO_FACTOR = 1000;

        private Ints cachedWhiteElos;
        private Ints cachedBlackElos;

        public EloWinRateBooster(AtomicReaderContext context) throws IOException {
            super(context);

            @SuppressWarnings("resource")
            AtomicReader reader = context.reader();
            cachedWhiteElos = FieldCache.DEFAULT.getInts(reader, FieldMapper.WHITE_ELO.toString(), false);
            cachedBlackElos = FieldCache.DEFAULT.getInts(reader, FieldMapper.BLACK_ELO.toString(), false);
        }

        @Override
        public float customScore(int docID, float subQueryScore, float valSrcScore) {
            int whiteElo = cachedWhiteElos.get(docID);
            int blackElo = cachedBlackElos.get(docID);
            float score = subQueryScore;
            if (whiteElo >= ELO_GRAND_MASTER) {
                score *= whiteElo / ELO_FACTOR;
            }
            if (blackElo >= ELO_GRAND_MASTER) {
                score *= blackElo / ELO_FACTOR;
            }
            return score;
        }
    }

}
