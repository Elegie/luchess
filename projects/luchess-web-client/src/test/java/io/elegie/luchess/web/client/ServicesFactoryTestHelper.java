package io.elegie.luchess.web.client;

import io.elegie.luchess.app.lucene4x.explore.GameListQueryImpl;
import io.elegie.luchess.app.lucene4x.explore.GameListQueryResultImpl;
import io.elegie.luchess.core.api.BuilderService;
import io.elegie.luchess.core.api.ExplorerService;
import io.elegie.luchess.core.api.IndexInfoService;
import io.elegie.luchess.core.api.ServicesFactory;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.api.explore.GameFindQuery;
import io.elegie.luchess.core.api.explore.GameFindQueryResult;
import io.elegie.luchess.core.api.explore.GameListQuery;
import io.elegie.luchess.core.api.explore.GameListQueryResult;
import io.elegie.luchess.core.api.explore.QueryException;
import io.elegie.luchess.core.domain.entities.Game;

import java.util.Properties;

/**
 * Test factory used for all controller tests.
 */
@SuppressWarnings("javadoc")
public class ServicesFactoryTestHelper implements ServicesFactory {

    public static final int TOTAL_GAMES = 10;
    public static final String GAME_ID = "id";

    private boolean throwExceptionOnBuild = false;
    private boolean throwExceptionOnCount = false;
    private boolean throwExceptionOnList = false;

    @Override
    public void initialize(Properties parameters) {
    }

    @Override
    public BuilderService getBuilderService() {
        return new BuilderService() {
            @Override
            public void index(SourceDataSet dataSet, IndexMonitor monitor) throws BuildException {
                if (throwExceptionOnBuild) {
                    throw new BuildException("Throwing exception for test purposes");
                }
            }
        };
    }

    @Override
    public ExplorerService getExplorerService() {
        return new ExplorerService() {
            @Override
            public GameListQuery createGameListQuery() {
                return new GameListQueryImpl(null) {
                    @Override
                    public GameListQueryResult execute() throws QueryException {
                        if (throwExceptionOnList) {
                            throw new QueryException("Throwing exception for test purposes");
                        }
                        return new GameListQueryResultImpl();
                    }
                };
            }

            @Override
            public GameFindQuery createGameFindQuery() {
                return new GameFindQuery() {
                    private boolean idSet = false;

                    @Override
                    public GameFindQueryResult execute() throws QueryException {
                        return new GameFindQueryResult() {
                            @Override
                            public Game getGame() {
                                if (idSet) {
                                    Game game = new Game();
                                    game.setId(GAME_ID);
                                    return game;
                                }
                                return null;
                            }
                        };
                    }

                    @Override
                    public void setId(String id) {
                        idSet = GAME_ID.equals(id);
                    }
                };
            }
        };
    }

    @Override
    public IndexInfoService getIndexInfoService() {
        return new IndexInfoService() {
            @Override
            public int getGamesCount() throws QueryException {
                if (throwExceptionOnCount) {
                    throw new QueryException("Throwing exception for test purposes");
                }
                return TOTAL_GAMES;
            }
        };
    }

    public void setThrowExceptionOnBuild(boolean throwExceptionOnBuild) {
        this.throwExceptionOnBuild = throwExceptionOnBuild;
    }

    public void setThrowExceptionOnCount(boolean throwExceptionOnCount) {
        this.throwExceptionOnCount = throwExceptionOnCount;
    }

    public void setThrowExceptionOnList(boolean throwExceptionOnList) {
        this.throwExceptionOnList = throwExceptionOnList;
    }
}
