package io.elegie.luchess.web.client.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class GameInfoTest {

    @Test
    public void testMutators() {
        String id = "gameInfoId";
        GameInfo info = new GameInfo();
        info.setId(id);
        assertEquals(id, info.getId());
    }
}
