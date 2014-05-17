package io.elegie.luchess.web.framework.presenting.freemarker;

/**
 * Contains all possible options for a FTL view. As for now, these options are
 * contained within the name of the view, but one could design other mechanisms
 * if this simple approach would prove too simplistic.
 */
@SuppressWarnings("javadoc")
public class FreemarkerViewOptions {

    /**
     * Attachment marker, to be included into the view name:
     * {@value #FTL_ATTACHMENT}.
     */
    public static final String FTL_ATTACHMENT = "attachment";

    /**
     * No cache marker, to be included into the view name: {@value #FTL_NOCACHE}
     * .
     */
    public static final String FTL_NOCACHE = "nocache";

    private String viewName;

    /**
     * @param viewName
     *            The name of the view from which the options can be retrieved.
     */
    public FreemarkerViewOptions(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public String getContentType() {
        FreemarkerContentTypes contentType = FreemarkerContentTypes.resolve(viewName);
        if (contentType == null) {
            String message = "Cannot determine content type from view name \"%s\".";
            message = String.format(message, viewName);
            throw new IllegalStateException(message);
        }
        return contentType.getType();
    }

    /**
     * @return True if the view should be sent back as an attachment.
     */
    public boolean isAttachment() {
        return viewName.contains(FTL_ATTACHMENT);
    }

    /**
     * @return True if the view should not be cached.
     */
    public boolean isNoCache() {
        return viewName.contains(FTL_NOCACHE);
    }

    /**
     * Creates a FTL view name with the provided name and type. By default, the
     * view is not an attachment, and may be cached.
     * 
     * @param viewName
     *            Name of the view.
     * @param type
     *            Type of the view.
     * @return The FTL view name, with options embedded.
     */
    public static String createViewName(String viewName, FreemarkerContentTypes type) {
        return createViewName(viewName, type, false, false);
    }

    /**
     * Creates a FTL view name with the provided name, type, attachment and
     * cache options.
     * 
     * @param viewName
     *            Name of the view.
     * @param type
     *            Type of the view.
     * @param isAttachment
     *            Whether the view should be sent back as an attachment.
     * @param isNoCache
     *            Whether the view should not be cached (for GET methods).
     * @return The FTL view name, with options embedded.
     */
    public static String createViewName(String viewName, FreemarkerContentTypes type, boolean isAttachment, boolean isNoCache) {
        if (viewName == null || type == null) {
            String message = "View name and type must no be null.";
            throw new IllegalArgumentException(message);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(viewName).append('.');
        if (isAttachment) {
            builder.append(FTL_ATTACHMENT).append('.');
        }
        if (isNoCache) {
            builder.append(FTL_NOCACHE).append('.');
        }
        builder.append(type.getExtension());
        return builder.toString();
    }

}
