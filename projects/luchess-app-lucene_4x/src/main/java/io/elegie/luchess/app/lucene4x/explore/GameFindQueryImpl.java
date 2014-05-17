package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.ReaderTemplate;
import io.elegie.luchess.app.lucene4x.index.mapping.FieldMapper;
import io.elegie.luchess.app.lucene4x.index.mapping.IndexMapper;
import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.GameFindQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Game;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

/**
 * A simple implementation of the {@link GameFindQuery}, which lets us retrieve
 * a game by its id, looking up the ID field of all documents, with a TermQuery.
 */
public class GameFindQueryImpl extends AbstractQueryImpl implements GameFindQuery {

    private String id;

    @SuppressWarnings("javadoc")
    public GameFindQueryImpl(IndexManager indexManager) {
        super(indexManager);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public GameFindQueryResult execute() throws QueryException {
        return (GameFindQueryResult) safeExecute(createReaderTemplate());
    }

    private ReaderTemplate<GameFindQueryResult> createReaderTemplate() {
        return new ReaderTemplate<GameFindQueryResult>(getIndexManager()) {
            @SuppressWarnings("resource")
            @Override
            protected GameFindQueryResult doRead(ReaderTemplateArg arg) throws IOException {
                IndexReader reader = arg.getIndexReader();
                Query query = new TermQuery(new Term(FieldMapper.ID.toString(), id));
                IndexSearcher searcher = new IndexSearcher(reader);
                TopDocs results = searcher.search(query, 1);
                Game game = null;
                if (results.scoreDocs.length > 0) {
                    game = IndexMapper.createGame(reader.document(results.scoreDocs[0].doc));
                }
                return new GameFindQueryResultImpl(game);
            }
        };
    }

}
