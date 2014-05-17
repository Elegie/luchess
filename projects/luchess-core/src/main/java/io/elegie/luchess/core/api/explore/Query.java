package io.elegie.luchess.core.api.explore;


/**
 * Specifies a domain query to be performed against the index. The Query model
 * has been inspired by the Command Pattern.
 * 
 * @param <T>
 *            The Result return by the Query.
 */
public interface Query<T extends QueryResult> {

    /**
     * Execute the query. We do not make any assumption on the result returned
     * by the query, and let implementations decide whether they want a
     * fully-loaded or lazily-loaded result set.
     * 
     * @return The result of the query, containing all relevant data.
     * @throws QueryException
     *             When the Query could not be executed, because of technical
     *             (non-domain) problems, such as a non-existing or unreachable
     *             index.
     */
    T execute() throws QueryException;

}
