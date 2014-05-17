package io.elegie.luchess.pgn.api;

/**
 * <p>
 * This exception is thrown when an invalid PGN text sequence has been
 * encountered, and therefore cannot be parsed. For instance, an author would
 * have meant the move "e4", but written "e40", which is not a valid move.
 * </p>
 * 
 * <p>
 * Note that parsers work with format only (i.e. validity), and not content
 * (i.e. legality). In other words, they check that the text is valid (moves are
 * recognized), but not whether it is legal (already occupied squares, illegal
 * castle...).
 * </p>
 */
@SuppressWarnings({ "serial", "javadoc" })
public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
