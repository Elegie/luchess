package io.elegie.luchess.app.lucene4x.explore;

import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.ReaderTemplate;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.api.explore.QueryResult;

import java.io.IOException;

/**
 * A base class for all queries, which provides access to the index manager, so
 * that they can properly query the index.
 */
public abstract class AbstractQueryImpl {

    private IndexManager indexManager;

    protected AbstractQueryImpl(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    protected IndexManager getIndexManager() {
        return indexManager;
    }

    /**
     * Helper execute method, used to wrap any IOException into a
     * QueryException.
     * 
     * @param readerTemplate
     *            The query code to run.
     * @return The result of the query.
     * @throws QueryException
     *             When the query code has failed.
     */
    protected QueryResult safeExecute(ReaderTemplate<? extends QueryResult> readerTemplate) throws QueryException {
        try {
            return readerTemplate.execute();
        } catch (IOException e) {
            String message = "Cannot execute query.";
            throw new QueryException(message, e);
        }
    }

}
