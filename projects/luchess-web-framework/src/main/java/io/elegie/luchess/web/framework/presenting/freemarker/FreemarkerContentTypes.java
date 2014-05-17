package io.elegie.luchess.web.framework.presenting.freemarker;

/**
 * Recognized content types for the FTL views.
 */
public enum FreemarkerContentTypes {

    /**
     * Content type "text/plain", with extension "txt.ftl".
     */
    TEXT("text/plain", "txt.ftl"),

    /**
     * Content type "text/json", with extension "json.ftl".
     */
    JSON("text/json", "json.ftl"),

    /**
     * Content type "text/html", with extension "html.ftl".
     */
    HTML("text/html", "html.ftl");

    private String type;
    private String extension;

    private FreemarkerContentTypes(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    /**
     * @return The string value of the content type, respecting standard types
     *         naming (e.g. "text/html").
     */
    public String getType() {
        return type;
    }

    /**
     * @return The extension of the view name of a given content type (e.g
     *         "html.ftl").
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param viewName
     *            The view name of the FTL view, from which to infer the content
     *            type.
     * @return The content type, or null if none could be resolved.
     */
    public static FreemarkerContentTypes resolve(String viewName) {
        for (FreemarkerContentTypes type : values()) {
            if (viewName.contains(type.getExtension())) {
                return type;
            }
        }
        return null;
    }

}
