package io.elegie.luchess.core.api.explore;

/**
 * A query to find certain game, by its id.
 */
public interface GameFindQuery extends Query<GameFindQueryResult> {

    /**
     * @param id
     *            The id of the game to be retrieved from the index.
     */
    void setId(String id);

}
