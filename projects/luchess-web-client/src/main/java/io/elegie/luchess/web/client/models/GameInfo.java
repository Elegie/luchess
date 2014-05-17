package io.elegie.luchess.web.client.models;

/**
 * A DTO for a game ID. This object may be used by any controller requiring a
 * game ID, such as a
 * {@link io.elegie.luchess.web.client.controllers.SaveController} or a
 * {@link io.elegie.luchess.web.client.controllers.LoadController}.
 */
@SuppressWarnings("javadoc")
public class GameInfo {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
