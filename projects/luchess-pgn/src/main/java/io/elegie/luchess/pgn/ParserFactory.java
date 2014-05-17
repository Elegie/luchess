package io.elegie.luchess.pgn;

import io.elegie.luchess.pgn.api.Parser;
import io.elegie.luchess.pgn.impl.ParserImpl;
import io.elegie.luchess.pgn.impl.grammars.MoveGrammar;
import io.elegie.luchess.pgn.impl.grammars.SimpleGameGrammar;

/**
 * The parser factory provides methods to construct and get parsers. One should
 * always use the factory, and never attempt to build the wanted parser
 * directly, as the factory may perform specific initialization steps.
 */
public final class ParserFactory {

    private ParserFactory() {
    }

    /**
     * @return A {@link io.elegie.luchess.pgn.impl.ParserImpl} initialized with
     *         the {@link io.elegie.luchess.pgn.impl.grammars.SimpleGameGrammar}
     */
    public static Parser createSimpleGameParser() {
        return new ParserImpl(new SimpleGameGrammar());
    }

    /**
     * @return A {@link io.elegie.luchess.pgn.impl.ParserImpl} initialized with
     *         the {@link io.elegie.luchess.pgn.impl.grammars.MoveGrammar}
     */
    public static Parser createMoveParser() {
        return new ParserImpl(new MoveGrammar());
    }

}
