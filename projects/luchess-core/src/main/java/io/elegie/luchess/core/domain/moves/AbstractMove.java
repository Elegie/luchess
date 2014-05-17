package io.elegie.luchess.core.domain.moves;

import io.elegie.luchess.core.domain.entities.Move;
import io.elegie.luchess.core.domain.entities.Vertex;

/**
 * All moves share common components:
 * <ul>
 * <li>They may all have a starting vertex, called <b>disambiguation</b>.</li>
 * <li>They must all have an ending vertex, called <b>target</b>.</li>
 * <li>The may capture an enemy piece.</li>
 * <li>The may threaten the enemy king with a check.</li>
 * </ul>
 * 
 * <p>
 * A castle move is a special move in terms of target and disambiguation, where
 * the disambiguation is the start square of the king, and the target its end
 * square.
 * </p>
 */
@SuppressWarnings("javadoc")
public abstract class AbstractMove extends Move {

    private Vertex disambiguation;
    private Vertex target;
    private char check;
    private boolean captures;

    /**
     * @return the text representation of the move, without the check mark.
     */
    public abstract String getUncheckedValue();

    /**
     * @return The complete text representation of the move.
     */
    @Override
    public final String getValue() {
        StringBuilder value = new StringBuilder(getUncheckedValue());
        if (check != 0) {
            value.append(check);
        }
        return value.toString();
    }

    public void setCheck(char check) {
        this.check = check;
    }

    public void setDisambiguation(Vertex disambiguation) {
        this.disambiguation = disambiguation;
    }

    public Vertex getDisambiguation() {
        return disambiguation;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public Vertex getTarget() {
        return target;
    }

    public boolean getCaptures() {
        return captures;
    }

    public void setCaptures(boolean captures) {
        this.captures = captures;
    }

}
