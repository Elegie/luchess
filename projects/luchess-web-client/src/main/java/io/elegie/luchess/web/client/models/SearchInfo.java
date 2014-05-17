package io.elegie.luchess.web.client.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link io.elegie.luchess.web.client.controllers.ListController}
 * . The object contains all possible search criteria, so that we can retrieve a
 * list of games matching these criteria.
 */
@SuppressWarnings("javadoc")
public class SearchInfo {

    private String name;
    private int elo;
    private int pageStart;
    private int pageCount;
    private String exec;
    private List<String> moves = new ArrayList<>();
    private String previous;
    private int current;
    private String refresh;
    private String next;

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {name: %s, elo: %s, pageStart: %s, pageCount: %s, exec: %s, moves: %s, previous: %s, current: %s, refresh: %s, next: %s}";
        return String.format(value, className, name, elo, pageStart, pageCount, exec, moves, previous, current, refresh, next);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

}
