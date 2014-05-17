package io.elegie.luchess.web.client.helpers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.web.client.ConfigurationSetupTest;
import io.elegie.luchess.web.client.ServicesFactoryTestHelper;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class GameHelperTest extends ConfigurationSetupTest {

    @Test
    public void testGameFound() throws QueryException {
        assertNotNull(GameHelper.getGame(ServicesFactoryTestHelper.GAME_ID));
    }

    @Test
    public void testGameNotFound() throws QueryException {
        assertNull(GameHelper.getGame("unknownId"));
    }

}
