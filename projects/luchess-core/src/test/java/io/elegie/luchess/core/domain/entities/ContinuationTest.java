package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertTrue;
import io.elegie.luchess.core.domain.moves.CastleMove;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ContinuationTest {

    private static final int BEFORE_INCREMENT = 0;
    private static final int AFTER_INCREMENT = 1;
    private static final int FINAL_VALUE = 2;

    private Continuation continuation;

    @Before
    public void setUp() {
        continuation = new Continuation();
    }

    @Test
    public void testWhite() {
        assertTrue(continuation.getTotalWhiteWins() == BEFORE_INCREMENT);
        continuation.incrementWhiteWins();
        assertTrue(continuation.getTotalWhiteWins() == AFTER_INCREMENT);
        continuation.setTotalWhiteWins(FINAL_VALUE);
        assertTrue(continuation.getTotalWhiteWins() == FINAL_VALUE);
    }

    @Test
    public void testBlack() {
        assertTrue(continuation.getTotalBlackWins() == BEFORE_INCREMENT);
        continuation.incrementBlackWins();
        assertTrue(continuation.getTotalBlackWins() == AFTER_INCREMENT);
        continuation.setTotalBlackWins(FINAL_VALUE);
        assertTrue(continuation.getTotalBlackWins() == FINAL_VALUE);
    }

    @Test
    public void testDraw() {
        assertTrue(continuation.getTotalDraws() == BEFORE_INCREMENT);
        continuation.incrementDraws();
        assertTrue(continuation.getTotalDraws() == AFTER_INCREMENT);
        continuation.setTotalDraws(FINAL_VALUE);
        assertTrue(continuation.getTotalDraws() == FINAL_VALUE);
    }

    @Test
    public void testUnfinished() {
        assertTrue(continuation.getTotalUnfinished() == BEFORE_INCREMENT);
        continuation.incrementUnfinished();
        assertTrue(continuation.getTotalUnfinished() == AFTER_INCREMENT);
        continuation.setTotalUnfinished(FINAL_VALUE);
        assertTrue(continuation.getTotalUnfinished() == FINAL_VALUE);
    }

    @Test
    public void testMove() {
        Move randomMove = new CastleMove(false);
        continuation.setMove(randomMove);
        assertTrue(continuation.getMove().equals(randomMove));
    }

}
