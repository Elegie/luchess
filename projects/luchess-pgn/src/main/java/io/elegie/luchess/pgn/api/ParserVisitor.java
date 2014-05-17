package io.elegie.luchess.pgn.api;

/**
 * <p>
 * When processing some PGN text, the {@link io.elegie.luchess.pgn.api.Parser}
 * will regularly call methods of the Visitor, when a particular PGN sequence is
 * recognized. For instance, the method {@link #visitTagPairName(String)} would
 * be called after a header has been processed (like a player's name, or an
 * elo's rating). The value passed to the method would be the value of the
 * header (the actual name or rating of the player).
 * </p>
 * 
 * <p>
 * Clients requesting the parsing of some PGN text will therefore be notified
 * progressively of all valid PGN entities encountered during the processing.
 * They should then apply their own domain logic, for instance building an
 * object representation of the content being parsed (such as a Game).
 * </p>
 */
public interface ParserVisitor {

    /**
     * @param value
     *            The name (key) of the tag, e.g. "White", "BlackElo". You
     *            should expect a tag value coming next.
     * @see io.elegie.luchess.pgn.impl.tokens.TagPairNameToken
     */
    void visitTagPairName(String value);

    /**
     * @param value
     *            The value of the tag (such as a player's name). Usually comes
     *            right after a tag name has been provided.
     * @see io.elegie.luchess.pgn.impl.tokens.TagPairValueToken
     */
    void visitTagPairValue(String value);

    /**
     * @param value
     *            A notation glyph, following a move, (such as an exclamation
     *            mark to indicate a good move).
     * @see io.elegie.luchess.pgn.impl.tokens.NAGToken
     */
    void visitNAG(String value);

    /**
     * @param value
     *            A comment in the game. Comments can come in two flavors:
     *            one-line or multi-lines, like in most programming languages.
     * @see io.elegie.luchess.pgn.impl.tokens.StandardCommentToken
     * @see io.elegie.luchess.pgn.impl.tokens.EndOfLineCommentToken
     */
    void visitComment(String value);

    /**
     * @param value
     *            A full variation, which may include most game-related tokens,
     *            such as moves, comments, and even inner variations. If needed,
     *            one could consider building a grammar specifying variations,
     *            and mount some dedicated parser upon it.
     * @see io.elegie.luchess.pgn.impl.tokens.VariationToken
     */
    void visitVariation(String value);

    /**
     * @param value
     *            A probable full move, unparsed. Further methods of this
     *            interface list components for a move (figurine, column...), as
     *            they are recognized by the PGN specification.
     * @see io.elegie.luchess.pgn.impl.tokens.UnstructuredMoveToken
     */
    void visitUnstructuredMove(String value);

    /**
     * @param value
     *            A value indicating the end of the game (e.g. 1-0 when White
     *            wins).
     * @see io.elegie.luchess.pgn.impl.tokens.TerminationToken
     */
    void visitTermination(String value);

    /**
     * @param value
     *            A string representing a king castle (small or big).
     * @see io.elegie.luchess.pgn.impl.tokens.CastleToken
     */
    void visitCastle(String value);

    /**
     * @param value
     *            A letter representing a figurine (e.g. K for king).
     * @see io.elegie.luchess.pgn.impl.tokens.FigurineToken
     */
    void visitFigurine(String value);

    /**
     * @param value
     *            A letter representing a column (from 'a' to 'h').
     * @see io.elegie.luchess.pgn.impl.tokens.ColumnToken
     */
    void visitColumn(String value);

    /**
     * @param value
     *            A digit representing a row (from '1' to '8').
     * @see io.elegie.luchess.pgn.impl.tokens.RowToken
     */
    void visitRow(String value);

    /**
     * @param value
     *            'x' when there is a capture.
     * @see io.elegie.luchess.pgn.impl.tokens.CaptureToken
     */
    void visitCapture(String value);

    /**
     * @param value
     *            '+' or '#' when there is a check.
     * @see io.elegie.luchess.pgn.impl.tokens.CheckToken
     */
    void visitCheck(String value);

    /**
     * @param value
     *            '=' when there is a promotion. Must be followed by a figurine
     *            letter, to indicate which piece the pawn is promoted into.
     * @see io.elegie.luchess.pgn.impl.tokens.PromotionToken
     */
    void visitPromotion(String value);
}
