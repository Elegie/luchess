package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertNotNull;
import io.elegie.luchess.web.client.Models;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SearchControllerTest {

    @Test
    public void testSearch() {
        assertNotNull(new SearchController().search().getModel().get(Models.BOARD.getValue()));
    }

}
