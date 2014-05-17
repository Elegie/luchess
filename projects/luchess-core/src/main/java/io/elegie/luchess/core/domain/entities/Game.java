package io.elegie.luchess.core.domain.entities;

/**
 * A game object. Moves are stored in a text format, for performance reasons.
 * The class also provides an id field, so that implementations may uniquely
 * distinguish their games, if needed.
 */
@SuppressWarnings("javadoc")
public class Game {

    private String id;
    private String white;
    private String black;
    private int whiteElo;
    private int blackElo;
    private Result result = Result.UNFINISHED;
    private MoveText moveText = new MoveText();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public int getWhiteElo() {
        return whiteElo;
    }

    public void setWhiteElo(int whiteElo) {
        this.whiteElo = whiteElo;
    }

    public int getBlackElo() {
        return blackElo;
    }

    public void setBlackElo(int blackElo) {
        this.blackElo = blackElo;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public MoveText getMoveText() {
        return moveText;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {id: %s,  white: %s, black: %s, whiteElo: %s, blackElo: %s, result: %s, moveText: %s}";
        return String.format(value, className, id, white, black, whiteElo, blackElo, result, moveText);
    }

    // --- Equals / HashCode --------------------------------------------------

    /**
     * A game equals another game if it has the same players and the same
     * sequence of moves.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        Game game = (Game) o;
        boolean whiteEquals = (white == null && game.white == null) || (white != null && white.equals(game.white));
        boolean blackEquals = (black == null && game.black == null) || (black != null && black.equals(game.black));
        return whiteEquals && blackEquals && moveText.equals(game.moveText);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int start = 17;
        int value = start;
        value = prime * value + (white == null ? 0 : white.hashCode());
        value = prime * value + (black == null ? 0 : black.hashCode());
        value = prime * value + moveText.hashCode();
        return value;
    }

}
