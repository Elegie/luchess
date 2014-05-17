package io.elegie.luchess.core.api.explore;

import java.util.List;

/**
 * A query to find a list of games, matching given criteria, including
 * pagination. Criteria should be combined using an AND operator.
 */
public interface GameListQuery extends Query<GameListQueryResult> {

    /**
     * @param white
     *            The name of the White player (case-insensitive).
     */
    void setWhite(String white);

    /**
     * @param black
     *            The name of the Black player (case-insensitive).
     */
    void setBlack(String black);

    /**
     * @param elo
     *            The minimum elo ranking of both players (when available).
     */
    void setMinElo(int elo);

    /**
     * @param moves
     *            The moves by which the searched game should start (i.e. their
     *            opening moves).
     */
    void setMoves(List<String> moves);

    /**
     * @param pageStart
     *            The requested page of games (games are grouped by pages, so as
     *            to avoid having too many games in one query). First page is
     *            page 1.
     */
    void setPageStart(int pageStart);

    /**
     * @param pageCount
     *            The number of games per page.
     */
    void setPageCount(int pageCount);

}
