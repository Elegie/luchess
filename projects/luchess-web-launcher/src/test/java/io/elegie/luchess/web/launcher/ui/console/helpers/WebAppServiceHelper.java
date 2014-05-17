package io.elegie.luchess.web.launcher.ui.console.helpers;

import io.elegie.luchess.web.launcher.services.api.WebApp;
import io.elegie.luchess.web.launcher.services.api.WebAppService;

/**
 * A simple service used to test the console.
 */
public class WebAppServiceHelper implements WebAppService {

    /**
     * The context path for the returned web app, if any.
     */
    public static final String CONTEXT_PATH = "/testWebAppServiceHelper";

    private boolean appFound;

    /**
     * @param appFound
     *            Should he getWebApp method return an object or null?
     */
    public WebAppServiceHelper(boolean appFound) {
        this.appFound = appFound;
    }

    @Override
    public WebApp getWebApp() {
        return appFound ? new WebApp("", CONTEXT_PATH) : null;
    }

}
