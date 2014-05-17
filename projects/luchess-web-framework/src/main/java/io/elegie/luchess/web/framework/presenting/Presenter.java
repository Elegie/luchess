package io.elegie.luchess.web.framework.presenting;

/**
 * A presenter is used to resolve a view name to an actual view object.
 */
public interface Presenter {

    /**
     * @param viewName
     *            The view to look up.
     * @return The initialized view object.
     */
    View resolve(String viewName);

}
