package io.elegie.luchess.web.client.models;

/**
 * A DTO for the {@link io.elegie.luchess.web.client.controllers.MoveController}
 * .
 */
@SuppressWarnings("javadoc")
public class MoveInfo {

    private String start;
    private String end;
    private String position;
    private String promotion;

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {start: %s, end: %s, position: %s, promotion: %s}";
        return String.format(value, className, start, end, position, promotion);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

}
