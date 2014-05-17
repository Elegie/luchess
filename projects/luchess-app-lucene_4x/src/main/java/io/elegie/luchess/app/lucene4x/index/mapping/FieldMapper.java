package io.elegie.luchess.app.lucene4x.index.mapping;

/**
 * Provides a list of all fields used for game documents.
 */
public enum FieldMapper {

    /**
     * The ID of the game. Each game must be unique, so that the user may
     * download and save a specific game.
     */
    ID("id"),

    /**
     * The name of the player playing White.
     */
    WHITE("White"),

    /**
     * The name of the player playing Black.
     */
    BLACK("Black"),

    /**
     * The elo rating of the player playing White.
     */
    WHITE_ELO("WhiteElo"),

    /**
     * The elo rating of the player playing Black.
     */
    BLACK_ELO("BlackElo"),

    /**
     * The whole sequence of moves of a game.
     */
    MOVE_TEXT("MoveText"),

    /**
     * The result of the game: white/black's victory, a draw, lr an unfinished
     * game.
     */
    RESULT("Result");

    private String value;

    private FieldMapper(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
