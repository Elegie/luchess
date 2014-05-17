package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.core.domain.entities.Board;
import io.elegie.luchess.core.domain.exceptions.IllegalMoveException;
import io.elegie.luchess.core.domain.moves.processors.MoveParser;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;
import io.elegie.luchess.web.client.models.SearchInfo;
import io.elegie.luchess.web.framework.payload.Model;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ListControllerTest extends ConfigurationSetupTest {

    // --- Query --------------------------------------------------------------

    @Test
    public void testListExec() throws IllegalMoveException {
        Model model = testSimpleQuery("exec", null, null);
        assertNotNull(model.get(Models.QUERY.getValue()));
    }

    @Test
    public void testListPrevious() throws IllegalMoveException {
        Model model = testSimpleQuery(null, "previous", null);
        assertNotNull(model.get(Models.QUERY.getValue()));
    }

    @Test
    public void testListNext() throws IllegalMoveException {
        Model model = testSimpleQuery(null, null, "next");
        assertNotNull(model.get(Models.QUERY.getValue()));
    }

    @Test
    public void testListError() throws IllegalMoveException {
        ((ServicesFactoryTestHelper) ClientContext.INSTANCE.getServicesFactory()).setThrowExceptionOnList(true);
        Model model = testSimpleQuery("exec", null, null);
        assertNotNull(model.get(Models.ERROR.getValue()));
    }

    private Model testSimpleQuery(String exec, String previous, String next) throws IllegalMoveException {
        int elo = 2500;
        List<String> moves = Arrays.asList("e4", "e5", "Nf3", "");
        List<String> movesOnBoard = Arrays.asList("e4", "e5");
        String name = "name";
        int pageCount = 10;
        int pageStart = 1;

        SearchInfo search = new SearchInfo();
        search.setElo(elo);
        search.setExec(exec);
        search.setPrevious(previous);
        search.setNext(next);
        search.setMoves(moves);
        search.setName(name);
        search.setPageCount(pageCount);
        search.setPageStart(pageStart);

        if (exec != null) {
            movesOnBoard = moves;
        }
        if (previous != null) {
            search.setCurrent(3);
        }
        if (next != null) {
            search.setCurrent(1);
        }

        Model model = new ListController().list(search).getModel();
        Board resultingBoard = new Board();
        resultingBoard.play(MoveParser.convert(movesOnBoard));
        assertEquals(search, model.get(Models.SEARCH.getValue()));
        assertEquals(resultingBoard, model.get(Models.BOARD.getValue()));

        return model;
    }

    // --- Move errors --------------------------------------------------------

    @Test
    public void testListInvalidMove() {
        testMoveError("e9");
    }

    @Test
    public void testListIllegalMove() {
        testMoveError("e5");
    }

    private void testMoveError(String move) {
        List<String> moves = Arrays.asList(move);
        SearchInfo search = new SearchInfo();
        search.setMoves(moves);
        Model model = new ListController().list(search).getModel();
        assertEquals(search, model.get(Models.SEARCH.getValue()));
        assertEquals(new Board(), model.get(Models.BOARD.getValue()));
        assertNotNull(model.get(Models.ERROR.getValue()));
    }
}
