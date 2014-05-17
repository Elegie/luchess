package io.elegie.luchess.pgn.api;

/**
 * This is a convenience class providing an empty implementation for all methods
 * of the {@link ParserVisitor} interface. Inherited classes only need to
 * override the methods they are interested into.
 */
public abstract class AbstractParserVisitor implements ParserVisitor {

    @Override
    public void visitTagPairName(String value) {
    }

    @Override
    public void visitTagPairValue(String value) {
    }

    @Override
    public void visitNAG(String value) {
    }

    @Override
    public void visitComment(String value) {
    }

    @Override
    public void visitVariation(String value) {
    }

    @Override
    public void visitUnstructuredMove(String value) {
    }

    @Override
    public void visitTermination(String value) {
    }

    @Override
    public void visitCastle(String value) {
    }

    @Override
    public void visitFigurine(String value) {
    }

    @Override
    public void visitColumn(String value) {
    }

    @Override
    public void visitRow(String value) {
    }

    @Override
    public void visitCapture(String value) {
    }

    @Override
    public void visitCheck(String value) {
    }

    @Override
    public void visitPromotion(String value) {
    }

}
