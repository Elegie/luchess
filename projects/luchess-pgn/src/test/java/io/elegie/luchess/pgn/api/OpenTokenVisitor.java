package io.elegie.luchess.pgn.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple visitor, which records each value passed to the visitor methods in a
 * local cache, indexed per Token Type.
 */
public class OpenTokenVisitor implements ParserVisitor {

    private final Map<TokenType, String> values = new HashMap<>();

    /**
     * @param type
     *            The Token Type for which we want to retrieve the visited
     *            value.
     * @return The value passed to the visitor during the parsing.
     */
    public String getValue(TokenType type) {
        return values.get(type);
    }

    // --- Implementation -----------------------------------------------------

    @Override
    public void visitTagPairName(String value) {
        values.put(TokenType.TAG_PAIR_NAME, value);
    }

    @Override
    public void visitTagPairValue(String value) {
        values.put(TokenType.TAG_PAIR_VALUE, value);
    }

    @Override
    public void visitNAG(String value) {
        values.put(TokenType.NAG, value);
    }

    @Override
    public void visitComment(String value) {
        values.put(TokenType.COMMENT, value);
    }

    @Override
    public void visitVariation(String value) {
        values.put(TokenType.VARIATION, value);
    }

    @Override
    public void visitUnstructuredMove(String value) {
        values.put(TokenType.UNSTRUCTURED_MOVE, value);
    }

    @Override
    public void visitTermination(String value) {
        values.put(TokenType.TERMINATION, value);
    }

    @Override
    public void visitCastle(String value) {
        values.put(TokenType.CASTLE, value);
    }

    @Override
    public void visitFigurine(String value) {
        values.put(TokenType.FIGURINE, value);
    }

    @Override
    public void visitColumn(String value) {
        values.put(TokenType.COLUMN, value);
    }

    @Override
    public void visitRow(String value) {
        values.put(TokenType.ROW, value);
    }

    @Override
    public void visitCapture(String value) {
        values.put(TokenType.CAPTURE, value);
    }

    @Override
    public void visitCheck(String value) {
        values.put(TokenType.CHECK, value);
    }

    @Override
    public void visitPromotion(String value) {
        values.put(TokenType.PROMOTION, value);
    }

}
