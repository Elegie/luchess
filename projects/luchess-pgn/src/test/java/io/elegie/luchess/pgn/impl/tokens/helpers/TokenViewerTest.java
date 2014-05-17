package io.elegie.luchess.pgn.impl.tokens.helpers;

import io.elegie.luchess.pgn.impl.grammars.MoveGrammar;
import io.elegie.luchess.pgn.impl.grammars.SimpleGameGrammar;
import io.elegie.luchess.pgn.impl.tokens.Token;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be used for debugging purposes. It prints a lovely view of a
 * grammar tree, so that one can visually assert whether all branching are
 * appropriate.
 */
@SuppressWarnings("javadoc")
public class TokenViewerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TokenViewerTest.class);

    private final Set<Token> visitedTokens = new HashSet<>();
    private StringBuilder builder;

    @Before
    public void setUp() {
        builder = new StringBuilder().append('\n');
    }

    @After
    public void tearDown() {
        LOG.info(builder.toString());
    }

    @Test
    @Ignore
    public void displayTokensForSimpleGrammar() {
        traverseGrammar(new SimpleGameGrammar().getRoot(), 0);
    }

    @Test
    @Ignore
    public void displayTokensForMoveGrammar() {
        traverseGrammar(new MoveGrammar().getRoot(), 0);
    }

    private void traverseGrammar(Token root, int indentLevel) {
        if (root == null) {
            return;
        }

        boolean visited = visitedTokens.contains(root);
        builder.append(getIndent(indentLevel));
        builder.append(root.getClass().getSimpleName() + (visited ? " (ref)" : " (new)"));
        builder.append('\n');

        if (!visited) {
            visitedTokens.add(root);
            for (Token successor : root.getSuccessors()) {
                traverseGrammar(successor, indentLevel + 1);
            }
        }
    }

    private String getIndent(int indentLevel) {
        final StringBuilder result = new StringBuilder();
        while (indentLevel > 0) {
            result.append("---");
            indentLevel--;
        }
        return result.toString();
    }

}
