package io.elegie.luchess.core.domain.moves.processors;

import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.moves.AbstractFigurineMove;
import io.elegie.luchess.core.domain.moves.AbstractMove;
import io.elegie.luchess.core.domain.moves.AbstractPawnMove;
import io.elegie.luchess.core.domain.moves.BishopMove;
import io.elegie.luchess.core.domain.moves.CastleMove;
import io.elegie.luchess.core.domain.moves.KingMove;
import io.elegie.luchess.core.domain.moves.KnightMove;
import io.elegie.luchess.core.domain.moves.PawnCaptureMove;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;
import io.elegie.luchess.core.domain.moves.QueenMove;
import io.elegie.luchess.core.domain.moves.RookMove;
import io.elegie.luchess.pgn.api.AbstractParserVisitor;
import io.elegie.luchess.pgn.api.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Move visitor in which data regarding the move being parsed are accumulated.
 */
class MoveParserVisitor extends AbstractParserVisitor {

    private static final Map<Figurine, Class<? extends AbstractFigurineMove>> FIGURINES_MOVES = new HashMap<>();

    static {
        FIGURINES_MOVES.put(Figurine.KNIGHT, KnightMove.class);
        FIGURINES_MOVES.put(Figurine.ROOK, RookMove.class);
        FIGURINES_MOVES.put(Figurine.BISHOP, BishopMove.class);
        FIGURINES_MOVES.put(Figurine.QUEEN, QueenMove.class);
        FIGURINES_MOVES.put(Figurine.KING, KingMove.class);
    }

    private Figurine figurine = null;
    private Figurine promotedFigurine = null;
    private boolean isCastle = false;
    private boolean isSmall = false;
    private boolean captures = false;
    private boolean promotes = false;
    private char check = 0;
    private char disambiguationCol = 0;
    private int disambiguationRow = 0;
    private char targetCol = 0;
    private int targetRow = 0;

    /**
     * @return The constructed move, or null if no move can be constructed.
     * @throws ParseException
     *             When the move cannot be constructed.
     */
    public Move getMove() throws ParseException {
        AbstractMove move = null;
        if (isCastle) {
            move = createCastleMove();
        } else {
            if (figurine == null || figurine.equals(Figurine.PAWN)) {
                move = createPawnMove();
            } else {
                move = createFigurineMove();
            }
            move.setDisambiguation(new Vertex(disambiguationCol, disambiguationRow));
            move.setTarget(new Vertex(targetCol, targetRow));
            move.setCaptures(captures);
        }
        if (check != 0) {
            move.setCheck(check);
        }
        return move;
    }

    private CastleMove createCastleMove() {
        return new CastleMove(isSmall);
    }

    private AbstractPawnMove createPawnMove() {
        AbstractPawnMove pawnMove = captures ? new PawnCaptureMove() : new PawnForwardMove();
        if (promotes) {
            pawnMove.setPromotion(promotedFigurine);
        }
        return pawnMove;
    }

    private AbstractFigurineMove createFigurineMove() throws ParseException {
        try {
            if (FIGURINES_MOVES.containsKey(figurine)) {
                return FIGURINES_MOVES.get(figurine).newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "Cannot create figurine move for %s.";
            message = String.format(message, figurine);
            throw new ParseException(message, e);
        }
        String message = "Unrecognized figurine: %s.";
        message = String.format(message, figurine);
        throw new ParseException(message);
    }

    // --- Visitor Implementation ---------------------------------------------

    @Override
    public void visitCastle(String value) {
        isCastle = true;
        isSmall = "O-O".equalsIgnoreCase(value);
    }

    @Override
    public void visitFigurine(String value) {
        Figurine visitedFigurine = Figurine.parse(value.charAt(0));
        if (promotes) {
            promotedFigurine = visitedFigurine;
        } else {
            figurine = visitedFigurine;
        }
    }

    /**
     * A move always have a target, but may not have a disambiguation. This
     * means that the first column visited is first assumed to be the target
     * one, but is changed into the disambiguated one when the visiting method
     * is called a second time.
     */
    @Override
    public void visitColumn(String value) {
        char col = value.charAt(0);
        if (targetCol != 0) {
            disambiguationCol = targetCol;
        }
        targetCol = col;
    }

    /**
     * Same logic as in the column visiting method.
     */
    @Override
    public void visitRow(String value) {
        int row = Integer.parseInt(value);
        if (targetRow != 0) {
            disambiguationRow = targetRow;
        }
        targetRow = row;
    }

    @Override
    public void visitCapture(String value) {
        captures = !value.isEmpty();
    }

    @Override
    public void visitCheck(String value) {
        check = value.charAt(0);
    }

    @Override
    public void visitPromotion(String value) {
        promotes = true;
    }

}
