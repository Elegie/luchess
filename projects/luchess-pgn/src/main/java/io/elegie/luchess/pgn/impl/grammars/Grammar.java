package io.elegie.luchess.pgn.impl.grammars;

import io.elegie.luchess.pgn.impl.tokens.Token;

/**
 * <p>
 * A Grammar aggregates some PGN entities (tokens) into a bigger PGN entity. For
 * instance, a simple move could be defined by a move number, followed by a dot,
 * followed by a move value: 1.e4.
 * </p>
 * 
 * <p>
 * Grammars are associated to {@link io.elegie.luchess.pgn.api.Parser}s. Thus, a
 * move parser will be associated to a move grammar, which recognizes strings
 * like 'e4', 'Nf3+'. Similarly, a game parser will be associated to a game
 * grammar, which includes more complicated PGN entities, like headers,
 * comments, variations and so forth.
 * </p>
 * 
 * <p>
 * The rules to which all grammars must abide have been defined in the <a
 * href="http://www6.chessclub.com/help/PGN-spec" target="_blank">PGN
 * specification</a>, which identifies all PGN tokens, and in which context they
 * may be used.
 * </p>
 */
public interface Grammar {

    /**
     * Each grammar can be viewed as a tree, made of PGN tokens. This method
     * returns the one and only root of the grammar, which generally has a set
     * of children which can be navigated by a parser.
     * 
     * @return Root token of the grammar
     */
    Token getRoot();

}
