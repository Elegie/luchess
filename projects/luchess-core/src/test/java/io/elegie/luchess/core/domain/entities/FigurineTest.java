package io.elegie.luchess.core.domain.entities;

import static io.elegie.luchess.core.domain.entities.Figurine.BISHOP;
import static io.elegie.luchess.core.domain.entities.Figurine.KING;
import static io.elegie.luchess.core.domain.entities.Figurine.KNIGHT;
import static io.elegie.luchess.core.domain.entities.Figurine.PAWN;
import static io.elegie.luchess.core.domain.entities.Figurine.QUEEN;
import static io.elegie.luchess.core.domain.entities.Figurine.ROOK;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class FigurineTest {

    private static final char[] values = new char[] { 'P', 'R', 'N', 'B', 'Q', 'K' };
    private static final Figurine[] figurines = new Figurine[] { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING };

    @Test
    public void testFigurineParsing() {
        for (int ii = 0; ii < values.length; ii++) {
            assertTrue(Figurine.parse(values[ii]).equals(figurines[ii]));
        }
        assertNull(Figurine.parse('Z')); // unknown figurine
    }

    @Test
    public void testFigurineValues() {
        for (int ii = 0; ii < figurines.length; ii++) {
            assertTrue(figurines[ii].toString().equals(Character.toString(values[ii])));
        }
    }

}
