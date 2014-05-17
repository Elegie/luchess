package io.elegie.luchess.web.client;

import io.elegie.luchess.web.framework.exceptions.ApplicationException;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ClientContextFactoryTest {

    private ClientContextFactory factory;

    @Before
    public void setUp() {
        factory = new ClientContextFactory();
    }

    @Test
    public void testCreationSuccess() throws ApplicationException {
        factory.create(ClientContextTest.createTestConfiguration());
    }

    @Test(expected = ApplicationException.class)
    public void testCreationFailure() throws ApplicationException {
        factory.create(null);
    }
}
