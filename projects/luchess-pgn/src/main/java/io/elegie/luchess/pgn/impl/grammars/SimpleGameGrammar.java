package io.elegie.luchess.pgn.impl.grammars;

import io.elegie.luchess.pgn.impl.tokens.ClosingTagPairToken;
import io.elegie.luchess.pgn.impl.tokens.Dot1Token;
import io.elegie.luchess.pgn.impl.tokens.Dot3Token;
import io.elegie.luchess.pgn.impl.tokens.EndOfFileToken;
import io.elegie.luchess.pgn.impl.tokens.EndOfLineCommentToken;
import io.elegie.luchess.pgn.impl.tokens.MoveTextStartToken;
import io.elegie.luchess.pgn.impl.tokens.NAGToken;
import io.elegie.luchess.pgn.impl.tokens.Number1Token;
import io.elegie.luchess.pgn.impl.tokens.NumberToken;
import io.elegie.luchess.pgn.impl.tokens.OpeningTagPairToken;
import io.elegie.luchess.pgn.impl.tokens.RootToken;
import io.elegie.luchess.pgn.impl.tokens.SpaceSeparatorToken;
import io.elegie.luchess.pgn.impl.tokens.StandardCommentToken;
import io.elegie.luchess.pgn.impl.tokens.TagPairNameToken;
import io.elegie.luchess.pgn.impl.tokens.TagPairValueToken;
import io.elegie.luchess.pgn.impl.tokens.TerminationWithNumber1Token;
import io.elegie.luchess.pgn.impl.tokens.TerminationWithoutNumber1Token;
import io.elegie.luchess.pgn.impl.tokens.Token;
import io.elegie.luchess.pgn.impl.tokens.UnstructuredMoveToken;
import io.elegie.luchess.pgn.impl.tokens.VariationToken;

/**
 * The game grammar is made of two sections:
 * <ul>
 * <li>
 * The metadata section is a kind of file header, it has information about
 * players, their ratings, the game and so on. These data are written thanks to
 * PGN so-called tag pairs: [TagName "TagValue"].</li>
 * <li>
 * The move text section lists all moves of the game, but can also hold
 * variations, comments and special annotations (NAG).</li>
 * </ul>
 * 
 * <p>
 * Please check the <a href="http://www6.chessclub.com/help/PGN-spec"
 * target="_blank">PGN specification</a> for more information about the PGN
 * format. Note that the grammar which we have implemented does not respect all
 * rules of the specification (which are too permissive, as the format was
 * intended to be human-readable), but only those leading to a
 * reasonably-formatted game.
 * </p>
 * 
 * <p>
 * The grammar is named "simple", because we do not parse the moves themselves,
 * for performance reasons. Note that we could do it if we needed it, though -
 * plugging in the {@link io.elegie.luchess.pgn.impl.grammars.MoveGrammar}.
 * </p>
 * 
 * <p>
 * The final grammar node can be represented as follows:
 * </p>
 * 
 * <pre>
 * RootToken (new)
 * ---SpaceSeparatorToken (new)
 * ------RootToken (ref)
 * ---OpeningTagPairToken (new)
 * ------TagPairNameToken (new)
 * ---------SpaceSeparatorToken (new)
 * ------------TagPairValueToken (new)
 * ---------------ClosingTagPairToken (new)
 * ------------------RootToken (ref)
 * ---MoveTextStartToken (new)
 * ------RootToken (new)
 * ---------SpaceSeparatorToken (new)
 * ------------RootToken (ref)
 * ---------EndOfLineCommentToken (new)
 * ------------RootToken (ref)
 * ---------StandardCommentToken (new)
 * ------------RootToken (ref)
 * ---------VariationToken (new)
 * ------------RootToken (ref)
 * ---------NAGToken (new)
 * ------------RootToken (ref)
 * ---------Number1Token (new)
 * ------------NumberToken (new)
 * ---------------Dot1Token (new)
 * ------------------SpaceSeparatorToken (new)
 * ---------------------UnstructuredMoveToken (new)
 * ------------------------SpaceSeparatorToken (new)
 * ---------------------------UnstructuredMoveToken (new)
 * ------------------------------SpaceSeparatorToken (new)
 * ---------------------------------RootToken (ref)
 * ---------------------------RootToken (new)
 * ------------------------------SpaceSeparatorToken (new)
 * ---------------------------------RootToken (ref)
 * ------------------------------EndOfLineCommentToken (new)
 * ---------------------------------RootToken (ref)
 * ------------------------------StandardCommentToken (new)
 * ---------------------------------RootToken (ref)
 * ------------------------------VariationToken (new)
 * ---------------------------------RootToken (ref)
 * ------------------------------NAGToken (new)
 * ---------------------------------RootToken (ref)
 * ------------------------------Number1Token (new)
 * ---------------------------------NumberToken (new)
 * ------------------------------------Dot3Token (new)
 * ---------------------------------------SpaceSeparatorToken (new)
 * ------------------------------------------UnstructuredMoveToken (ref)
 * ---------------------------------------UnstructuredMoveToken (ref)
 * ---------------------------------Dot3Token (ref)
 * ---------------------------------TerminationWithNumber1Token (new)
 * ------------------------------TerminationWithoutNumber1Token (new)
 * ------------------------------NumberToken (ref)
 * ------------------UnstructuredMoveToken (ref)
 * ------------Dot1Token (ref)
 * ------------TerminationWithNumber1Token (ref)
 * ---------TerminationWithoutNumber1Token (ref)
 * ---------NumberToken (ref)
 * ---EndOfFileToken (new)
 * </pre>
 */
public class SimpleGameGrammar implements Grammar {

    @Override
    public Token getRoot() {
        final Token root = new RootToken();
        addSpaceSeparator(root);
        addTagPair(root);
        addMoveText(root);
        addEndOfFile(root);
        return root;
    }

    // --- Helpers ----------------------------------------------------------

    private void addTagPair(Token origin) {
        final Token tagOpening = new OpeningTagPairToken();
        final Token tagName = new TagPairNameToken();
        final Token spaceSeparatorInTag = new SpaceSeparatorToken();
        final Token tagValue = new TagPairValueToken();
        final Token tagClosing = new ClosingTagPairToken();

        origin.addSuccessor(tagOpening);
        tagOpening.addSuccessor(tagName);
        tagName.addSuccessor(spaceSeparatorInTag);
        spaceSeparatorInTag.addSuccessor(tagValue);
        tagValue.addSuccessor(tagClosing);
        tagClosing.addSuccessor(origin);
    }

    private void addMoveText(Token origin) {
        final Token gameStart = new MoveTextStartToken();
        final Token moveText = new RootToken();

        origin.addSuccessor(gameStart);
        gameStart.addSuccessor(moveText);

        addSpaceSeparator(moveText);
        addMoveOrTermination(moveText);
    }

    private void addGameSideInserts(Token origin) {
        addEndOfLineComment(origin);
        addStandardComment(origin);
        addVariation(origin);
        addNAG(origin);
    }

    private void addSpaceSeparator(Token origin) {
        final Token separator = new SpaceSeparatorToken();
        origin.addSuccessor(separator);
        separator.addSuccessor(origin);
    }

    /**
     * We do not want any look-ahead in our parser (for performance matters),
     * but there's a price to it: the grammar can become complex, because we
     * have to precisely set up each continuation for each node.
     * 
     * In this method, some special attention is given to the number '1', which
     * may indicate the start of the move text section, the start of a move
     * number (1., 12.) or the start of a termination token (1-0, 1/2-1/2). The
     * order of the node branches matter here.
     */
    private void addMoveOrTermination(Token origin) {

        // Before each move or termination, we can have some inserts
        addGameSideInserts(origin);

        // A move text starts with the number 1
        final Token number1 = new Number1Token();
        origin.addSuccessor(number1);

        // Let us form a move
        final Token numberMoveFirst = new NumberToken();
        final Token dotMoveFirst = new Dot1Token();
        final Token dotMoveFirstSeparator = new SpaceSeparatorToken();
        final Token moveFirst = new UnstructuredMoveToken();
        final Token moveFirstSeparator = new SpaceSeparatorToken();
        final Token moveSecond = new UnstructuredMoveToken();
        final Token moveSecondSeparator = new SpaceSeparatorToken();

        // Let us add the first move, immediately followed by the second move
        numberMoveFirst.addSuccessor(dotMoveFirst);
        dotMoveFirst.addSuccessor(dotMoveFirstSeparator);
        dotMoveFirstSeparator.addSuccessor(moveFirst);
        dotMoveFirst.addSuccessor(moveFirst);
        moveFirst.addSuccessor(moveFirstSeparator);
        moveFirstSeparator.addSuccessor(moveSecond);
        moveSecond.addSuccessor(moveSecondSeparator);
        moveSecondSeparator.addSuccessor(origin);
        // numberMoveFirst shall be added last to origin

        // Let us add the "0-1" and "*" termination
        // "0" is also a number, but not a move starter
        // So the termination must be processed before
        // the NumberToken for move numbers
        final Token terminationWithoutNumber1 = new TerminationWithoutNumber1Token();
        origin.addSuccessor(terminationWithoutNumber1);

        // Many things can come between the first and the second move
        // After these, we have get back on the move, or terminate the game
        final Token inserts = new RootToken();
        final Token number1ForMoveSecondOrTermination = new Number1Token();
        final Token numberXForMoveSecond = new NumberToken();
        final Token dot3ForMoveSecond = new Dot3Token();
        final Token dot3Separator = new SpaceSeparatorToken();
        moveFirstSeparator.addSuccessor(inserts);
        addSpaceSeparator(inserts);
        addGameSideInserts(inserts);
        inserts.addSuccessor(number1ForMoveSecondOrTermination);
        inserts.addSuccessor(terminationWithoutNumber1);
        inserts.addSuccessor(numberXForMoveSecond);
        number1ForMoveSecondOrTermination.addSuccessor(numberXForMoveSecond);
        number1ForMoveSecondOrTermination.addSuccessor(dot3ForMoveSecond);
        numberXForMoveSecond.addSuccessor(dot3ForMoveSecond);
        dot3ForMoveSecond.addSuccessor(dot3Separator);
        dot3Separator.addSuccessor(moveSecond);
        dot3ForMoveSecond.addSuccessor(moveSecond);

        // Now, the second termination token (1-0, 1/2-1/2)
        final Token terminationWithNumber1 = new TerminationWithNumber1Token();
        number1.addSuccessor(numberMoveFirst);
        number1.addSuccessor(dotMoveFirst);
        number1.addSuccessor(terminationWithNumber1);
        number1ForMoveSecondOrTermination.addSuccessor(terminationWithNumber1);

        // Add in correct order
        origin.addSuccessor(numberMoveFirst);
    }

    private void addEndOfLineComment(Token origin) {
        final Token endOfLineComment = new EndOfLineCommentToken();
        origin.addSuccessor(endOfLineComment);
        endOfLineComment.addSuccessor(origin);
    }

    private void addStandardComment(Token origin) {
        final Token standardComment = new StandardCommentToken();
        origin.addSuccessor(standardComment);
        standardComment.addSuccessor(origin);
    }

    private void addVariation(Token origin) {
        final Token variation = new VariationToken();
        origin.addSuccessor(variation);
        variation.addSuccessor(origin);
    }

    private void addNAG(Token origin) {
        final Token nag = new NAGToken();
        origin.addSuccessor(nag);
        nag.addSuccessor(origin);
    }

    private void addEndOfFile(Token origin) {
        final Token eof = new EndOfFileToken();
        origin.addSuccessor(eof);
    }

}
