package io.elegie.luchess.pgn.api;

/**
 * List of token types, in order to try and reuse program logic. Indeed, our
 * token Visitor, as per the Visitor Design Pattern, breaks encapsulation and
 * use token-named methods, which prevents us from building generic algorithms.
 * This enumeration is here to help smooth things a bit.
 */
@SuppressWarnings("javadoc")
public enum TokenType {
    TAG_PAIR_NAME, //
    TAG_PAIR_VALUE, //
    NAG, //
    COMMENT, //
    VARIATION, //
    UNSTRUCTURED_MOVE, //
    TERMINATION, //
    CASTLE, //
    FIGURINE, //
    COLUMN, //
    ROW, //
    CAPTURE, //
    CHECK, //
    PROMOTION;
}
