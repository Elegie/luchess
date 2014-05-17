package io.elegie.luchess.web.client;

import io.elegie.luchess.web.framework.payload.Model;
import io.elegie.luchess.web.framework.routing.Navigation;

/**
 * This enumeration holds all model keys, which may be used in our templates.
 */
@SuppressWarnings("javadoc")
public enum Models {

    NAVIGATION("nav"),
    MONITOR("monitor"),
    NUM_GAMES("numGames"),
    ERROR("error"),
    BOARD("board"),
    SEARCH("search"),
    QUERY("query"),
    GAME("game"),
    MOVE("move"),
    MOVES("moves"),
    HAS_PASSWORD("hasPassword"),
    PAGE_START_DEFAULT("pageStartDefault"),
    PAGE_COUNT_DEFAULT("pageCountDefault"),

    MSG_ERROR_BUILD("errorBuild"),
    MSG_ERROR_BUILD_PASSWORD("errorBuildPassword"),
    MSG_ERROR_SEARCH("errorSearch"),
    MSG_ERROR_SEARCH_PARSE("errorSearchParse"),
    MSG_ERROR_SEARCH_ILLEGAL("errorSearchIllegal"),
    MSG_ERROR_SAVE("errorSave"),
    MSG_ERROR_LOAD("errorLoad"),
    MSG_ERROR_MOVE("errorMove");

    private String value;

    private Models(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * @return An empty model.
     */
    public static Model createEmptyModel() {
        return new Model();
    }

    /**
     * @return A model which contains navigation rules.
     */
    public static Model createModelWithNavigation() {
        Model model = createEmptyModel();
        model.put(NAVIGATION.getValue(), new Navigation(ClientContext.INSTANCE.getRouter()));
        return model;
    }

}
