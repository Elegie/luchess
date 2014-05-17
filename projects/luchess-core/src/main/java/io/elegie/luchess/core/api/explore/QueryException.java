package io.elegie.luchess.core.api.explore;

/**
 * When the query cannot be created or executed.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class QueryException extends Exception {

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
