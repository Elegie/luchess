package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.domain.entities.Figurine;
import io.elegie.luchess.core.domain.entities.Position;
import io.elegie.luchess.core.domain.entities.Vertex;
import io.elegie.luchess.core.domain.exceptions.InvalidPositionException;
import io.elegie.luchess.core.domain.exceptions.InvalidVertexException;
import io.elegie.luchess.core.domain.moves.AbstractMove;
import io.elegie.luchess.core.domain.moves.processors.MoveResolver;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.client.models.MoveInfo;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This controller is used to resolve a move, building a move object and
 * returning its unchecked value, from a given position, starting and ending
 * vertices, and possibly a promotion figurine (if the move to be resolved a is
 * pawn move).
 * </p>
 * 
 * <p>
 * It is used to translate clicked coordinates on the board into a valid
 * representation of the move.
 * </p>
 */
public class MoveController {

    private static final Logger LOG = LoggerFactory.getLogger(MoveController.class);

    /**
     * @param moveInfo
     *            Data used to infer the move object.
     * @return The translated move value.
     */
    @Controller
    public Result find(MoveInfo moveInfo) {
        Model model = Models.createEmptyModel();
        AbstractMove move = createMove(moveInfo);
        String value = "";
        String error = "";
        if (move != null) {
            value = move.getUncheckedValue();
        }
        if (value.isEmpty()) {
            error = Models.MSG_ERROR_MOVE.getValue();
        }
        model.put(Models.MOVE.getValue(), value);
        model.put(Models.ERROR.getValue(), error);
        return new Result(Views.MOVE.getName(), model);
    }

    private AbstractMove createMove(MoveInfo moveInfo) {
        MoveResolver resolver = new MoveResolver();
        if (!addDisambiguation(resolver, moveInfo)) {
            return null;
        }
        if (!addTarget(resolver, moveInfo)) {
            return null;
        }
        if (!addPosition(resolver, moveInfo)) {
            return null;
        }
        if (!addPromotion(resolver, moveInfo)) {
            return null;
        }
        return resolver.resolve();
    }

    private boolean addDisambiguation(MoveResolver resolver, MoveInfo moveInfo) {
        String start = moveInfo.getStart();
        if (start != null) {
            try {
                resolver.setDisambiguation(Vertex.create(start));
                return true;
            } catch (InvalidVertexException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return false;
    }

    private boolean addTarget(MoveResolver resolver, MoveInfo moveInfo) {
        String end = moveInfo.getEnd();
        if (end != null) {
            try {
                resolver.setTarget(Vertex.create(end));
                return true;
            } catch (InvalidVertexException e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return false;
    }

    private boolean addPosition(MoveResolver resolver, MoveInfo moveInfo) {
        try {
            resolver.setPosition(Position.deserialize(moveInfo.getPosition()));
            return true;
        } catch (InvalidPositionException e) {
            LOG.warn(e.getMessage(), e);
        }
        return false;
    }

    private boolean addPromotion(MoveResolver resolver, MoveInfo moveInfo) {
        String promotion = moveInfo.getPromotion();
        if (promotion != null && !promotion.isEmpty()) {
            Figurine promotedFigurine = Figurine.parse(promotion.charAt(0));
            if (promotedFigurine != null) {
                resolver.setPromotedFigurine(promotedFigurine);
            } else {
                return false;
            }
        }
        return true;
    }
}
