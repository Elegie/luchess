package io.elegie.luchess.web.client.controllers;

import static org.junit.Assert.assertEquals;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.models.MoveInfo;
import io.elegie.luchess.web.framework.payload.Model;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class MoveControllerTest {

    private MoveInfo moveInfo;
    private Position startPos;

    @Before
    public void setUp() {
        startPos = new Position();
        startPos.init();
        moveInfo = new MoveInfo();
        moveInfo.setPosition(Position.serialize(startPos));
    }

    @Test
    public void testFindMove() {
        moveInfo.setStart("g1");
        moveInfo.setEnd("f3");
        Model model = new MoveController().find(moveInfo).getModel();
        assertEquals("Nf3", model.get(Models.MOVE.getValue()));
        assertEquals("", model.get(Models.ERROR.getValue()));
    }

    @Test
    public void testInvalidMove() {
        moveInfo.setStart("f1");
        moveInfo.setEnd("g3");
        testError();
    }

    @Test
    public void testMissingDisambiguation() {
        testError();
    }

    @Test
    public void testInvalidDisambiguation() {
        moveInfo.setStart("g9");
        testError();
    }

    @Test
    public void testMissingTarget() {
        moveInfo.setStart("g1");
        testError();
    }

    @Test
    public void testInvalidTarget() {
        moveInfo.setStart("g1");
        moveInfo.setEnd("t3");
        testError();
    }

    @Test
    public void testMissingPosition() {
        moveInfo.setStart("g1");
        moveInfo.setEnd("f3");
        moveInfo.setPosition("");
        testError();
    }

    @Test
    public void testPromotion() {
        moveInfo.setStart("f1");
        moveInfo.setEnd("g3");
        moveInfo.setPromotion("Q");
        testError();
    }

    @Test
    public void testInvalidPromotion() {
        moveInfo.setStart("f1");
        moveInfo.setEnd("g3");
        moveInfo.setPromotion("Z");
        testError();
    }

    private void testError() {
        Model model = new MoveController().find(moveInfo).getModel();
        assertEquals("", model.get(Models.MOVE.getValue()));
        assertEquals(Models.MSG_ERROR_MOVE.getValue(), model.get(Models.ERROR.getValue()));
    }
}
