package io.elegie.luchess.core.indexing.workflow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.indexing.workflow.GameBuilder;
import io.elegie.luchess.pgn.ParserFactory;
import io.elegie.luchess.pgn.api.ParseException;
import io.elegie.luchess.pgn.impl.helpers.ParserHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests that our simple builder visitor is properly fed by the parser. The goal
 * is not to test the parser itself though, as other tests have been designed
 * for that very purpose.
 */
public class GameBuilderTest {

    /**
     * Checks that all fields have been allocated.
     * 
     * @throws FileNotFoundException
     *             The resource has not been found. Should not happen, as we
     *             reference a test resource.
     * @throws IOException
     *             The resource cannot be read.
     * @throws ParseException
     *             The resource cannot be parsed.
     */
    @Test
    public void testBuild() throws FileNotFoundException, IOException, ParseException {
        GameBuilder builder = new GameBuilder();
        ParserHelper.testResource("game.build.complete", ParserFactory.createSimpleGameParser(), builder);
        assertTrue(builder.isComplete());

        Game game = builder.getGame();
        assertNotNull(game.getWhite());
        assertNotNull(game.getBlack());
        assertNotNull(game.getResult());
        assertNotNull(game.getMoveText());
        assertTrue(game.getWhiteElo() > 0);
        assertTrue(game.getBlackElo() > 0);
    }

    /**
     * Checks that the builder has been fed at least a result and a move text.
     */
    @Test
    public void testBuildIsComplete() {
        GameBuilder builder = new GameBuilder();
        assertFalse(builder.isComplete());
        builder.visitTermination("1-0");
        assertFalse(builder.isComplete());
        builder.visitUnstructuredMove("e4");
        assertTrue(builder.isComplete());
    }

}
