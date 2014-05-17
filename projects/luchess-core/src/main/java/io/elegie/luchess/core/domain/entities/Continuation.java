package io.elegie.luchess.core.domain.entities;

/**
 * A continuation associates some statistics to a given move. It is typically
 * used in a game tree to suggest alternative moves in a given position, and
 * provide some statistics about following variations (i.e. which color tends to
 * win in the given position).
 */
@SuppressWarnings("javadoc")
public class Continuation {

    private Move move;
    private int totalWhiteWins;
    private int totalBlackWins;
    private int totalDraws;
    private int totalUnfinished;

    public void incrementWhiteWins() {
        totalWhiteWins++;
    }

    public void incrementBlackWins() {
        totalBlackWins++;
    }

    public void incrementDraws() {
        totalDraws++;
    }

    public void incrementUnfinished() {
        totalUnfinished++;
    }

    public void setTotalWhiteWins(int whiteWins) {
        this.totalWhiteWins = whiteWins;
    }

    public void setTotalBlackWins(int blackWins) {
        this.totalBlackWins = blackWins;
    }

    public void setTotalDraws(int draws) {
        this.totalDraws = draws;
    }

    public void setTotalUnfinished(int unfinished) {
        this.totalUnfinished = unfinished;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public int getTotalWhiteWins() {
        return totalWhiteWins;
    }

    public int getTotalBlackWins() {
        return totalBlackWins;
    }

    public int getTotalDraws() {
        return totalDraws;
    }

    public int getTotalUnfinished() {
        return totalUnfinished;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {move: %s, totalWhiteWins: %s, totalBlackWins: %s, totalDraws: %s, totalUnfinished: %s}";
        return String.format(value, className, move, totalWhiteWins, totalBlackWins, totalDraws, totalUnfinished);
    }

}
