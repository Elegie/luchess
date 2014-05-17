package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ResultTest {

    @Test
    public void testIntValuesSuccess() {
        assertEquals(Result.BLACK_WINS, Result.parse(-1));
        assertEquals(Result.DRAW, Result.parse(0));
        assertEquals(Result.WHITE_WINS, Result.parse(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntValuesFailure() {
        Result.parse(42);
    }

    @Test
    public void testPgnValuesSuccess() {
        assertEquals(Result.BLACK_WINS, Result.parse("0-1"));
        assertEquals(Result.DRAW, Result.parse("1/2-1/2"));
        assertEquals(Result.WHITE_WINS, Result.parse("1-0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPgnValuesFailure() {
        Result.parse("1-1");
    }

}
