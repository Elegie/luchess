package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.helpers.MoveHelper;
import io.elegie.luchess.core.domain.moves.PawnForwardMove;

import org.junit.Test;

/**
 * Tests common methods of all moves.
 */
public class MoveTest {

    /**
     * Moves are equal when their PGN value is the same.
     */
    @Test
    public void testEquals() {
        PawnForwardMove e4_1 = MoveHelper.createPawnForwardMove('e', 4);
        PawnForwardMove e4_2 = MoveHelper.createPawnForwardMove('e', 4);
        PawnForwardMove d4 = MoveHelper.createPawnForwardMove('d', 4);

        assertTrue("Identify check", e4_1.equals(e4_1));
        assertFalse("Type check", e4_1.equals("e4"));
        assertTrue("Values are equal", e4_1.equals(e4_2));
        assertFalse("Values differ", d4.equals(e4_1));
    }
}
