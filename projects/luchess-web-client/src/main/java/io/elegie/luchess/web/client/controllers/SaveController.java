package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.Views;
import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerView;

import java.util.Arrays;
import java.util.List;

/**
 * Quite similar to the {@link LoadController}, this controller returns a game
 * matching a provided game ID. The view name chosen by the controller ensures
 * that the result will be an attached file.
 */
public class SaveController extends AbstractGetGameController {

    private static final String ATTACHMENT_EXTENSION = ".pgn";

    @Override
    protected Models errorOnInvalidGame() {
        return Models.MSG_ERROR_SAVE;
    }

    @Override
    protected Result send(Game game) {
        Model model = Models.createEmptyModel();
        List<String> moves = Arrays.asList(game.getMoveText().getValue().split(Character.toString(MoveText.SEPARATOR)));
        model.put(FreemarkerView.ATTACHMENT_KEY, game.getId() + ATTACHMENT_EXTENSION);
        model.put(Models.GAME.getValue(), game);
        model.put(Models.MOVES.getValue(), moves);
        return new Result(Views.SAVE.getName(), model);
    }
}
