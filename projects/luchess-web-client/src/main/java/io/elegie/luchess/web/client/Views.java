package io.elegie.luchess.web.client;

import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.HTML;
import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.JSON;
import static io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerContentTypes.TEXT;
import io.elegie.luchess.web.framework.presenting.freemarker.FreemarkerViewOptions;

/**
 * Contains all view names for our application. These names must be consistent
 * with the actual names of the view files.
 */
@SuppressWarnings("javadoc")
public enum Views {

    INDEX(FreemarkerViewOptions.createViewName("index", HTML)),
    PROGRESS(FreemarkerViewOptions.createViewName("progress", JSON, false, true)),
    COUNT(FreemarkerViewOptions.createViewName("count", JSON, false, true)),
    SEARCH(FreemarkerViewOptions.createViewName("search", HTML)),
    SAVE(FreemarkerViewOptions.createViewName("save", TEXT, true, false)),
    MOVE(FreemarkerViewOptions.createViewName("move", JSON)),
    ERROR(FreemarkerViewOptions.createViewName("error", HTML));

    private String viewName;

    private Views(String viewName) {
        this.viewName = viewName;
    }

    public String getName() {
        return viewName;
    }

}
