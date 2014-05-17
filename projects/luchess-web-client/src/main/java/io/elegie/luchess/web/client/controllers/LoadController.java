package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.models.SearchInfo;
import io.elegie.luchess.web.framework.payload.Result;

import java.util.Arrays;

/**
 * Finds and retrieves the full game with an ID matching the one provided, to be
 * loaded into the regular search view.
 */
public class LoadController extends AbstractGetGameController {

    @Override
    protected Models errorOnInvalidGame() {
        return Models.MSG_ERROR_LOAD;
    }

    @Override
    protected Result send(Game game) {
        SearchInfo search = new SearchInfo();
        search.setCurrent(1);
        search.setMoves(Arrays.asList(game.getMoveText().getValue().split(Character.toString(MoveText.SEPARATOR))));
        Result result = new ListController().list(search);
        result.getModel().put(Models.GAME.getValue(), game);
        return result;
    }

}
