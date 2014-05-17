package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Board;
import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.moves.processors.MoveParser;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.client.helpers.TimedGameListQuery;
import io.elegie.luchess.web.client.models.SearchInfo;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes the search, and returns a list of games matching the search
 * criteria.
 */
public class ListController {

    private static final Logger LOG = LoggerFactory.getLogger(ListController.class);

    /**
     * @param search
     *            Contains all search criteria.
     * @return The list of games matching the criteria, to be displayed below
     *         the search view.
     */
    @Controller
    public Result list(SearchInfo search) {
        Model model = Models.createModelWithNavigation();
        model.put(Models.SEARCH.getValue(), search);
        model.put(Models.BOARD.getValue(), new Board());
        List<Move> moves = new ArrayList<>();
        for (String move : search.getMoves()) {
            if (move == null || move.isEmpty()) {
                break;
            }
            Move moveObject = MoveParser.convert(move);
            if (moveObject != null) {
                moves.add(MoveParser.convert(move));
            } else {
                model.put(Models.ERROR.getValue(), Models.MSG_ERROR_SEARCH_PARSE.getValue());
            }
        }
        try {
            insertMoves(model, search, moves);
        } catch (IllegalMoveException e) {
            String message = "Cannot play moves.";
            LOG.warn(message, e);
            model.put(Models.ERROR.getValue(), Models.MSG_ERROR_SEARCH_ILLEGAL.getValue());
        }
        try {
            insertQuery(model, search, moves);
        } catch (QueryException e) {
            String message = "Cannot execute query.";
            LOG.warn(message, e);
            model.put(Models.ERROR.getValue(), Models.MSG_ERROR_SEARCH.getValue());
        }
        return new Result(Views.SEARCH.getName(), model);
    }

    private void insertMoves(Model model, SearchInfo search, List<Move> moves) throws IllegalMoveException {
        boolean inSearch = search.getExec() != null;
        List<Move> target = moves;
        if (inSearch) {
            search.setCurrent(0);
        } else {
            int offset = 0;
            if (search.getPrevious() != null) {
                offset = -1;
            }
            if (search.getNext() != null) {
                offset = +1;
            }

            int current = search.getCurrent();
            int minMove = 1;
            int maxMove = moves.size();
            if (current < minMove || current > maxMove) {
                current = maxMove;
                search.setCurrent(current);
            }
            target = moves.subList(0, current);

            int index = current + offset;
            if (index >= minMove && index <= maxMove) {
                search.setCurrent(index);
                target = moves.subList(0, index);
            }
        }

        Board board = (Board) model.get(Models.BOARD.getValue());
        board.play(target);
    }

    private void insertQuery(Model model, SearchInfo search, List<Move> moves) throws QueryException {
        List<String> validMoves = new ArrayList<>();
        for (Move move : moves) {
            validMoves.add(move.getValue());
        }

        GameListQuery query = ClientContext.INSTANCE.getServicesFactory().getExplorerService().createGameListQuery();
        query = new TimedGameListQuery(query);
        query.setBlack(search.getName());
        query.setWhite(search.getName());
        query.setMoves(validMoves);
        query.setMinElo(search.getElo());
        query.setPageStart(search.getPageStart());
        query.setPageCount(search.getPageCount());
        model.put(Models.QUERY.getValue(), query.execute());
    }
}
