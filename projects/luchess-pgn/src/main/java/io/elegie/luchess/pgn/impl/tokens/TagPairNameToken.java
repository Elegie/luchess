package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

/**
 * Tag pairs are key/value pairs representing a game metadata. The name of the
 * pair is always a string, only made of the 26 Latin letters (lower and upper
 * case allowed).
 * 
 * <code>
 * [foo "bar"]
 * </code>
 */
public class TagPairNameToken extends SetToken {

    /**
     * Key for the name of the white player: {@value #WHITE}.
     */
    public static final String WHITE = "White";

    /**
     * Key for the name of the black player: {@value #BLACK}.
     */
    public static final String BLACK = "Black";

    /**
     * Key for the elo of the white player: {@value #WHITE_ELO}.
     */
    public static final String WHITE_ELO = "WhiteElo";

    /**
     * Key for the elo of the black player: {@value #BLACK_ELO}.
     */
    public static final String BLACK_ELO = "BlackElo";

    /**
     * Key for the result: {@value #RESULT}.
     */
    public static final String RESULT = "Result";

    @Override
    protected boolean isInSet(int data) {
        return (data >= 'a' && data <= 'z') || (data >= 'A' && data <= 'Z');
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitTagPairName(value);
    }

}
