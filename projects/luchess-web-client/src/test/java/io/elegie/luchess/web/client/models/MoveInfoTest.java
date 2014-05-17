package io.elegie.luchess.web.client.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class MoveInfoTest {

    @Test
    public void testMutators() {
        String start = "e7";
        String end = "e8";
        String promotion = "Q";
        String position = "xxx";
        MoveInfo info = new MoveInfo();
        info.setStart(start);
        info.setEnd(end);
        info.setPromotion(promotion);
        info.setPosition(position);
        assertEquals(start, info.getStart());
        assertEquals(end, info.getEnd());
        assertEquals(promotion, info.getPromotion());
        assertEquals(position, info.getPosition());
    }

}
