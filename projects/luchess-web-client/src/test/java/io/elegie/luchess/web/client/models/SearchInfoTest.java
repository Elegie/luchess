package io.elegie.luchess.web.client.models;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SearchInfoTest {

    @Test
    public void testMutators() {
        String name = "name";
        int elo = 2500;
        int pageStart = 1;
        int pageCount = 10;
        String exec = "exec";
        List<String> moves = Arrays.asList("e4", "e5");
        String previous = "previous";
        int current = 1;
        String refresh = "refresh";
        String next = "next";
        SearchInfo info = new SearchInfo();
        info.setName(name);
        info.setElo(elo);
        info.setPageStart(pageStart);
        info.setPageCount(pageCount);
        info.setExec(exec);
        info.setMoves(moves);
        info.setPrevious(previous);
        info.setCurrent(current);
        info.setRefresh(refresh);
        info.setNext(next);
        assertEquals(name, info.getName());
        assertEquals(elo, info.getElo());
        assertEquals(pageStart, info.getPageStart());
        assertEquals(pageCount, info.getPageCount());
        assertEquals(exec, info.getExec());
        assertEquals(moves, info.getMoves());
        assertEquals(previous, info.getPrevious());
        assertEquals(current, info.getCurrent());
        assertEquals(refresh, info.getRefresh());
        assertEquals(next, info.getNext());
    }
}
