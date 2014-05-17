package io.elegie.luchess.core.api.helpers;

import io.elegie.luchess.core.domain.entities.MoveText;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.domain.entities.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create games with configured data, so as to make
 * testing data readily available to functional tests.
 */
@SuppressWarnings("javadoc")
public final class GameHelper {

    private GameHelper() {
    }

    public static List<Game> createNewGames(int count) {
        List<Game> games = new ArrayList<>();
        for (int ii = 1; ii <= count; ii++) {
            games.add(createNewGame(ii, "e4"));
        }
        return games;
    }

    public static Game createNewGame(int suffix, String firstMove) {
        Game game = new Game();
        game.setWhite("White" + suffix);
        game.setBlack("Black" + suffix);
        game.setWhiteElo(2600 + suffix);
        game.setBlackElo(2500 + suffix);
        game.setResult(suffix % 2 == 0 ? Result.WHITE_WINS : Result.BLACK_WINS);
        game.getMoveText().addValue(firstMove);
        return game;
    }

    public static Game createNewGame(String white, String black, int whiteElo, int blackElo, Result result,
            String moves) {
        Game game = new Game();
        game.setWhite(white);
        game.setBlack(black);
        game.setWhiteElo(whiteElo);
        game.setBlackElo(blackElo);
        game.setResult(result);
        MoveText moveText = game.getMoveText();
        for (String move : moves.split("\\s")) {
            moveText.addValue(move);
        }
        return game;
    }
}
