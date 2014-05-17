package io.elegie.luchess.core.indexing.adapters.game;

import io.elegie.luchess.core.api.build.SourceDataUnit;
import io.elegie.luchess.core.domain.entities.Game;
import io.elegie.luchess.core.indexing.adapters.AbstractSourceDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A source data set made around a list of games.
 */
public class GameSourceDataSet extends AbstractSourceDataSet {

    /**
     * @param games
     *            Games to be wrapped in the data set.
     */
    public GameSourceDataSet(List<Game> games) {
        List<SourceDataUnit> dataUnits = new ArrayList<>();
        for (Game game : games) {
            dataUnits.add(new GameSourceDataUnit(game));
        }
        setDataUnitsIterator(dataUnits.iterator());
    }

}
