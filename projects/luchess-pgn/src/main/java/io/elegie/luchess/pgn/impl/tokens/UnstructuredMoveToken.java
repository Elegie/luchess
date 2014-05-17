package io.elegie.luchess.pgn.impl.tokens;

import io.elegie.luchess.pgn.api.ParserVisitor;

import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * A move can be quite complex to parse, as it includes definition of pieces,
 * board coordinates, captures, promotions and checks. Also, a move valid from a
 * format point of view may not be legal from a game point of view. As we do not
 * actually play moves, but simply compare them together, we have introduced
 * here the UnstructuredMoveToken, which simply recognizes characters which,
 * when properly composed together, form a probable valid move (but not
 * necessarily valid, as the order of authorized characters in not considered).
 * </p>
 * 
 * <p>
 * Note that some PGN files add appreciation marks on moves (e.g. "1.e4!",
 * "2.e5?"). These marks are not strictly valid from a PGN point of view, so
 * they are not included here.
 * </p>
 */
public class UnstructuredMoveToken extends SetToken {

    private static final Set<Character> VALID_START_CHARS = new TreeSet<>();
    private static final Set<Character> VALID_MOVE_CHARS = new TreeSet<>();

    static {
        VALID_START_CHARS.add(CastleToken.CASTLE_NODE);
        for (int ii = 0; ii < FigurineToken.FIGURINES.length; ii++) {
            VALID_START_CHARS.add(FigurineToken.FIGURINES[ii]);
        }
        for (int ii = 0; ii < ColumnToken.COLS.length; ii++) {
            VALID_START_CHARS.add(ColumnToken.COLS[ii]);
        }

        VALID_MOVE_CHARS.addAll(VALID_START_CHARS);
        VALID_MOVE_CHARS.add(CastleToken.CASTLE_NODE_SEPARATOR);
        VALID_MOVE_CHARS.add(CaptureToken.CAPTURE);
        VALID_MOVE_CHARS.add(PromotionToken.PROMOTION);
        for (int ii = 0; ii < RowToken.ROWS.length; ii++) {
            VALID_MOVE_CHARS.add(RowToken.ROWS[ii]);
        }
        for (int ii = 0; ii < CheckToken.VALUES.length; ii++) {
            VALID_MOVE_CHARS.add(CheckToken.VALUES[ii]);
        }
    }

    @Override
    protected boolean isInSet(int data) {
        return VALID_MOVE_CHARS.contains((char) data);
    }

    @Override
    public boolean accepts(int data) {
        return VALID_START_CHARS.contains((char) data);
    }

    @Override
    public void visitToken(ParserVisitor visitor, String value) {
        visitor.visitUnstructuredMove(value);
    }

}
