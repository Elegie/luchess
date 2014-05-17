package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

/**
 * Intended for super classes to let their child classes decide which visitor
 * method to call.
 */
public interface DelegateVisitor {

    /**
     * @param visitor
     *            The visitor to be notified. Child class should select the
     *            appropriate visitor method.
     * @param value
     *            The value to pass to the visitor method.
     */
    void visitToken(ParserVisitor visitor, String value);

}
