package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ColorTest {

    @Test
    public void testColorValues() {
        assertEquals(Color.WHITE.toString(), "W");
        assertEquals(Color.BLACK.toString(), "B");
    }

    @Test
    public void testColorParsing() {
        assertEquals(Color.WHITE, Color.parse('W'));
        assertEquals(Color.BLACK, Color.parse('B'));
        assertNull(Color.parse('Z'));
    }

    @Test
    public void testColorInversion() {
        assertEquals(Color.WHITE.invert(), Color.BLACK);
        assertEquals(Color.BLACK.invert(), Color.WHITE);
    }

}
