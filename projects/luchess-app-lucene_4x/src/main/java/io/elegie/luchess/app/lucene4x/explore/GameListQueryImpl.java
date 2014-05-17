package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.ReaderTemplate;
import io.elegie.luchess.app.lucene4x.index.mapping.IndexMapper;
import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Our implementation of the {@link GameListQuery}. This class acts as a
 * controller, delegating the actual query building to a
 * {@link GameListQueryBuilder}, creating a {@link GameListQueryCollector} and
 * {@link GameListQuerySimilarity} that let us collect continuations, then
 * executing the query against a properly configured index searcher.
 */
public class GameListQueryImpl extends AbstractQueryImpl implements GameListQuery {

    private static final int DEFAULT_PAGE_START = 1;
    private static final int DEFAULT_PAGE_COUNT = 10;

    private String white;
    private String black;
    private int elo;
    private List<String> moves = new ArrayList<>();
    private int pageStart = DEFAULT_PAGE_START;
    private int pageCount = DEFAULT_PAGE_COUNT;

    @SuppressWarnings("javadoc")
    public GameListQueryImpl(IndexManager indexManager) {
        super(indexManager);
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {white: %s, black: %s, elo: %s, moves: %s, pageStart: %s, pageCount: %s}";
        return String.format(value, className, white, black, elo, moves, pageStart, pageCount);
    }

    // --- Parameters ---------------------------------------------------------

    /**
     * The name for White (case-insensitive). Many names can be specified, if
     * separated by a comma. Exact match can be requested by enclosing the name
     * within double quotes.
     */
    @Override
    public void setWhite(String white) {
        this.white = white;
    }

    /**
     * Same rules as for the name for White.
     */
    @Override
    public void setBlack(String black) {
        this.black = black;
    }

    @Override
    public void setMinElo(int elo) {
        this.elo = elo;
    }

    @Override
    public void setMoves(List<String> moves) {
        if (moves == null) {
            String message = "Moves cannot be null. Pass an empty list if needed.";
            throw new IllegalArgumentException(message);
        }
        this.moves = moves;
    }

    @Override
    public void setPageStart(int pageStart) {
        this.pageStart = (pageStart <= 0) ? DEFAULT_PAGE_START : pageStart;
    }

    @Override
    public void setPageCount(int pageCount) {
        this.pageCount = (pageCount <= 0) ? DEFAULT_PAGE_COUNT : pageCount;
    }

    // --- Execution ----------------------------------------------------------

    @Override
    public GameListQueryResult execute() throws QueryException {
        return (GameListQueryResult) safeExecute(createReaderTemplate());
    }

    private ReaderTemplate<GameListQueryResult> createReaderTemplate() {
        return new ReaderTemplate<GameListQueryResult>(getIndexManager()) {
            @Override
            protected GameListQueryResult doRead(ReaderTemplateArg arg) throws IOException {
                GameListQueryBuilder queryBuilder = new GameListQueryBuilder(arg.getVersion(), arg.getAnalyzer(), arg.getAnalysisDepth());
                Query query = queryBuilder.createQuery(white, black, elo, moves);
                GameListQueryCollector collector = new GameListQueryCollector(pageStart, pageCount);
                GameListQuerySimilarity similarity = new GameListQuerySimilarity(collector);
                IndexSearcher searcher = new IndexSearcher(arg.getIndexReader());
                searcher.setSimilarity(similarity);
                searcher.search(query, collector);
                return createResult(arg.getIndexReader(), collector);
            }
        };
    }

    private GameListQueryResultImpl createResult(IndexReader indexReader, GameListQueryCollector collector) throws IOException {
        GameListQueryResultImpl result = new GameListQueryResultImpl();
        result.setGames(convertGames(indexReader, collector.getTopDocs()));
        result.setContinuations(collector.getContinuations());
        result.setTotalWhiteWins(collector.getTotalWhiteWins());
        result.setTotalBlackWins(collector.getTotalBlackWins());
        result.setTotalDraws(collector.getTotalDraws());
        result.setTotalUnfinished(collector.getTotalUnfinished());
        return result;
    }

    private List<Game> convertGames(IndexReader reader, TopDocs topDocs) throws IOException {
        List<Game> result = new ArrayList<>();
        ScoreDoc[] docs = topDocs.scoreDocs;
        for (int ii = 0; ii < docs.length; ii++) {
            int docID = docs[ii].doc;
            result.add(IndexMapper.createGame(reader.document(docID)));
        }
        return result;
    }

}
