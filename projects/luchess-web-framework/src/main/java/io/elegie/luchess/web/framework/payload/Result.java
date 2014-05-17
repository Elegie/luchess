package io.elegie.luchess.web.framework.payload;

/**
 * <p>
 * The result of the invocation of a controller method. The result contains the
 * name of the view to be displayed, as well as the model to be fed to the view.
 * </p>
 * 
 * <p>
 * Both the view name and model must not be null.
 * </p>
 */
@SuppressWarnings("javadoc")
public class Result {

    private String viewName;
    private Model model;

    /**
     * @param viewName
     *            The name of the view to display.
     * @param model
     *            The model to feed to the view object.
     */
    public Result(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;

        if (viewName == null || viewName.isEmpty() || model == null) {
            String message = "View name (%s) and model (%s) must be valid.";
            message = String.format(message, viewName, model);
            throw new IllegalArgumentException(message);
        }
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result result = (Result) o;
        return viewName.equals(result.viewName) && model.equals(result.model);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int start = 17;
        int value = start;
        value = prime * value + viewName.hashCode();
        value = prime * value + model.hashCode();
        return value;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        String value = "%s {viewName: %s, model: %s}";
        return String.format(value, className, viewName, model);
    }

}
