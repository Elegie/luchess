package io.elegie.luchess.core.domain.moves.processors;

import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.pgn.ParserFactory;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.api.Parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser which lets us transform a valid PGN string into a
 * {@link io.elegie.luchess.core.domain.entities.Move}
 */
public final class MoveParser {

    private static final Logger LOG = LoggerFactory.getLogger(MoveParser.class);

    /**
     * Singleton of our internal parser. The parser obtained through the factory
     * is assumed to be thread-safe, as per its specification.
     */
    private static final Parser INSTANCE = ParserFactory.createMoveParser();

    private MoveParser() {
    }

    /**
     * Converts a single move string expression into a
     * {@link io.elegie.luchess.core.domain.entities.Move} object.
     * 
     * @param move
     *            The PGN string for the move (e.g. Nexf3+).
     * @return The {@link io.elegie.luchess.core.domain.entities.Move} object,
     *         or null if the string cannot be parsed into a move.
     */
    public static Move convert(String move) {
        Reader reader = new StringReader(move);
        MoveParserVisitor visitor = new MoveParserVisitor();
        Move result = null;
        try {
            INSTANCE.digest(reader, visitor);
            result = visitor.getMove();
        } catch (IOException | ParseException e) {
            String message = "Cannot parse into a move: %s";
            message = String.format(message, move);
            LOG.warn(message, e);
        }
        return result;
    }

    /**
     * Converts a list of move string representations into a list of
     * {@link io.elegie.luchess.core.domain.entities.Move} objects.
     * 
     * The method stops when an empty or null string is met in the list,
     * returning the already processed moves.
     * 
     * @param source
     *            The list of PGN moves.
     * @return The list of {@link io.elegie.luchess.core.domain.entities.Move}
     *         objects.
     */
    public static List<Move> convert(List<String> source) {
        List<Move> target = new ArrayList<>();
        for (String move : source) {
            if (move == null || move.isEmpty()) {
                break;
            }
            target.add(convert(move));
        }
        return target;
    }

}
