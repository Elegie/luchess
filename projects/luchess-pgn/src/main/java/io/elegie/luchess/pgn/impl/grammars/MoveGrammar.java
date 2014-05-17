package io.elegie.luchess.pgn.impl.grammars;

import io.elegie.luchess.pgn.impl.tokens.CaptureToken;
import io.elegie.luchess.pgn.impl.tokens.CastleToken;
import io.elegie.luchess.pgn.impl.tokens.CheckToken;
import io.elegie.luchess.pgn.impl.tokens.ColumnToken;
import io.elegie.luchess.pgn.impl.tokens.EndOfFileToken;
import io.elegie.luchess.pgn.impl.tokens.FigurineToken;
import io.elegie.luchess.pgn.impl.tokens.PromotionToken;
import io.elegie.luchess.pgn.impl.tokens.RootToken;
import io.elegie.luchess.pgn.impl.tokens.RowToken;
import io.elegie.luchess.pgn.impl.tokens.Token;

/**
 * <p>
 * This grammar describes how a chess move can be represented in the PGN format.
 * There are many types of moves to consider: castle moves, pawn moves and
 * figurine moves.
 * </p>
 * 
 * <p>
 * Castle moves are the easier to parse, as they match exactly "O-O" for the
 * small castle, and "O-O-O" for the big one. We only need one token for this.
 * </p>
 * 
 * Other moves have roughly the same structure:
 * <ul>
 * <li>A figurine letter, representing the figurine being moved. For pawns, the
 * figurine should be omitted (though some formats still want to use the 'P'
 * letter). Possible figurine letters are 'K' for King, 'Q' for Queen, 'B' for
 * Bishop, 'N' for Knight and 'R' for Rook.</li>
 * <li>An optional disambiguation, i.e. a column, a row, or both, which identify
 * the starting vertex of the move. This disambiguation is needed when the
 * target vertex can be reached by several pieces, and not only one piece.</li>
 * <li>An optional capture, when the piece being moved captures an enemy piece.</li>
 * <li>The target vertex, made of a column, then a row. Both - column and row -
 * are always mentioned. For instance, the pawn move "e4" indicates the target
 * vertex.</li>
 * <li>Pawn moves may also include a promotion, i.e. the sign '=' followed by a
 * figurine letter.</li>
 * <li>Eventually, all moves can be followed by a check sign, either the "+"
 * sign for the king check, or the "#" sign for the checkmate.</li>
 * </ul>
 * 
 * <p>
 * To sum it up, the following moves are valid: e4, exf8=Q+, Nf3, R4e3, Q2xf7+.
 * </p>
 * 
 * <p>
 * The final grammar node can be represented as follows:
 * </p>
 * 
 * <pre>
 * RootToken (new)
 * ---CastleToken (new)
 * ------CheckToken (new)
 * ---------EndOfFileToken (new)
 * ------EndOfFileToken (new)
 * ---ColumnToken (new)
 * ------RowToken (new)
 * ---------CheckToken (new)
 * ------------EndOfFileToken (new)
 * ---------PromotionToken (new)
 * ------------FigurineToken (new)
 * ---------------CheckToken (new)
 * ------------------EndOfFileToken (new)
 * ---------------EndOfFileToken (new)
 * ---------EndOfFileToken (new)
 * ------CaptureToken (new)
 * ---------ColumnToken (new)
 * ------------RowToken (new)
 * ---------------CheckToken (new)
 * ------------------EndOfFileToken (new)
 * ---------------EndOfFileToken (new)
 * ---------------PromotionToken (new)
 * ------------------FigurineToken (new)
 * ---------------------CheckToken (new)
 * ------------------------EndOfFileToken (new)
 * ---------------------EndOfFileToken (new)
 * ---FigurineToken (new)
 * ------ColumnToken (new)
 * ---------ColumnToken (new)
 * ------------RowToken (new)
 * ---------------CheckToken (new)
 * ------------------EndOfFileToken (new)
 * ---------------EndOfFileToken (new)
 * ---------RowToken (new)
 * ------------ColumnToken (ref)
 * ------------CheckToken (new)
 * ---------------EndOfFileToken (new)
 * ------------EndOfFileToken (new)
 * ------------CaptureToken (new)
 * ---------------ColumnToken (new)
 * ------------------RowToken (new)
 * ---------------------CheckToken (new)
 * ------------------------EndOfFileToken (new)
 * ---------------------EndOfFileToken (new)
 * ---------CaptureToken (new)
 * ------------ColumnToken (new)
 * ---------------RowToken (new)
 * ------------------CheckToken (new)
 * ---------------------EndOfFileToken (new)
 * ------------------EndOfFileToken (new)
 * ------RowToken (new)
 * ---------ColumnToken (ref)
 * ---------CaptureToken (new)
 * ------------ColumnToken (new)
 * ---------------RowToken (new)
 * ------------------CheckToken (new)
 * ---------------------EndOfFileToken (new)
 * ------------------EndOfFileToken (new)
 * ------CaptureToken (new)
 * ---------ColumnToken (new)
 * ------------RowToken (new)
 * ---------------CheckToken (new)
 * ------------------EndOfFileToken (new)
 * ---------------EndOfFileToken (new)
 * </pre>
 */
public class MoveGrammar implements Grammar {

    @Override
    public Token getRoot() {
        final Token root = new RootToken();
        addCastleBranch(root);
        addPawnBranch(root);
        addFigurineBranch(root);
        return root;
    }

    // --- Helpers ----------------------------------------------------------

    private void addCheck(Token root) {
        final Token check = new CheckToken();
        root.addSuccessor(check);
        addEnd(check);
    }

    private void addEnd(Token root) {
        final Token end = new EndOfFileToken();
        root.addSuccessor(end);
    }

    private void addCapture(Token root, boolean allowPromotion) {
        final Token capture = new CaptureToken();
        final Token colTarget = new ColumnToken();
        final Token rowTarget = new RowToken();

        root.addSuccessor(capture);
        capture.addSuccessor(colTarget);
        colTarget.addSuccessor(rowTarget);

        addCheck(rowTarget);
        addEnd(rowTarget);
        if (allowPromotion) {
            addPromotion(rowTarget);
        }
    }

    private void addPromotion(Token root) {
        final Token promotion = new PromotionToken();
        final Token promotedFigurine = new FigurineToken();

        root.addSuccessor(promotion);
        promotion.addSuccessor(promotedFigurine);

        addCheck(promotedFigurine);
        addEnd(promotedFigurine);
    }

    // --- Branches ----------------------------------------------------------

    private void addCastleBranch(Token root) {
        final Token castle = new CastleToken();

        root.addSuccessor(castle);

        addCheck(castle);
        addEnd(castle);
    }

    private void addPawnBranch(Token root) {
        final Token pawnCol = new ColumnToken();
        final Token pawnRow = new RowToken();

        root.addSuccessor(pawnCol);
        pawnCol.addSuccessor(pawnRow);

        addCapture(pawnCol, true);
        addCheck(pawnRow);
        addPromotion(pawnRow);
        addEnd(pawnRow);
    }

    private void addFigurineBranch(Token root) {
        final Token figurine = new FigurineToken();
        final Token disambiguationCol = new ColumnToken();
        final Token disambiguationRow = new RowToken();
        final Token disambiguationOrTargetRow = new RowToken();
        final Token targetCol = new ColumnToken();
        final Token targetRow = new RowToken();

        root.addSuccessor(figurine);
        figurine.addSuccessor(disambiguationCol);
        figurine.addSuccessor(disambiguationRow);
        disambiguationCol.addSuccessor(targetCol);
        disambiguationCol.addSuccessor(disambiguationOrTargetRow);
        disambiguationOrTargetRow.addSuccessor(targetCol);
        disambiguationRow.addSuccessor(targetCol);
        targetCol.addSuccessor(targetRow);

        addCheck(targetRow);
        addCheck(disambiguationOrTargetRow);
        addEnd(targetRow);
        addEnd(disambiguationOrTargetRow);
        addCapture(figurine, false);
        addCapture(disambiguationCol, false);
        addCapture(disambiguationRow, false);
        addCapture(disambiguationOrTargetRow, false);
    }

}
