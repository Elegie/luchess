package io.elegie.luchess.core.domain.entities;

import io.elegie.luchess.pgn.impl.tokens.TerminationToken;

/**
 * Represents the end result of a game: one of the player wins, the game ends
 * into a draw, or is unfinished.
 */
@SuppressWarnings("javadoc")
public enum Result {

    WHITE_WINS(1, TerminationToken.WHITE_WINS),
    DRAW(0, TerminationToken.DRAW),
    BLACK_WINS(-1, TerminationToken.BLACK_WINS),
    UNFINISHED(Integer.MIN_VALUE, TerminationToken.UNFINISHED);

    private int intValue;
    private String strValue;

    private Result(int intValue, String strValue) {
        this.intValue = intValue;
        this.strValue = strValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return strValue;
    }

    public static Result parse(int value) {
        for (Result result : values()) {
            if (result.getIntValue() == value) {
                return result;
            }
        }
        String message = "Unrecognized result int value (%s).";
        message = String.format(message, value);
        throw new IllegalArgumentException(message);
    }

    public static Result parse(String value) {
        for (Result result : values()) {
            if (result.getStringValue().equalsIgnoreCase(value)) {
                return result;
            }
        }
        String message = "Unrecognized result string value (%s).";
        message = String.format(message, value);
        throw new IllegalArgumentException(message);
    }

}
