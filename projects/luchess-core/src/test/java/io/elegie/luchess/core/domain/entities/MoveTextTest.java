package io.elegie.luchess.core.domain.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests that a MoveText properly accumulates its values, and takes them into
 * account in its identity functions.
 */
@SuppressWarnings("javadoc")
public class MoveTextTest {

    @Test
    public void testMoveTextEmpty() {
        assertTrue(new MoveText().isEmpty());
    }

    @Test
    public void testValueBuilding() {
        String[] values = new String[] { "foo", "bar", "baz" };
        StringBuilder concatenatedValue = new StringBuilder();
        MoveText moveText = new MoveText();
        for (String value : values) {
            moveText.addValue(value);
            concatenatedValue.append(value).append(Character.toString(MoveText.SEPARATOR));
        }
        assertEquals(concatenatedValue.substring(0, concatenatedValue.length() - 1), moveText.getValue());
    }

    @Test
    public void testEquals() {
        MoveText first = new MoveText();
        assertTrue(first.equals(first));
        assertFalse(first.equals(null));
        assertFalse(first.equals(new String()));

        MoveText second = new MoveText();
        assertTrue(first.equals(second));
        first.addValue("foo");
        first.addValue("bar");
        assertFalse(first.equals(second));
        second.addValue("foo");
        second.addValue("bar");
        assertTrue(first.equals(second));
    }

    @Test
    public void testHashCode() {
        MoveText moveText = new MoveText();
        moveText.addValue("foo");
        assertEquals(Arrays.asList("foo").hashCode(), moveText.hashCode());
    }

}
