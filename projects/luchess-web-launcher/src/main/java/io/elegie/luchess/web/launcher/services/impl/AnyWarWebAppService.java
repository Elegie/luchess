package io.elegie.luchess.web.launcher.services.impl;

import io.elegie.luchess.web.launcher.services.api.WebApp;
import io.elegie.luchess.web.launcher.services.api.WebAppService;

import java.io.File;

/**
 * A container for any Java web app located in the same directory as the
 * launcher jar file.
 */
public class AnyWarWebAppService implements WebAppService {

    private static final String JAR = ".jar";
    private static final String WAR = ".war";
    private static final String CONTEXT_PATH = "/";

    private String path;

    /**
     * @param path
     *            The path to the web app directory.
     */
    public AnyWarWebAppService(String path) {
        this.path = path;
    }

    @Override
    public WebApp getWebApp() {
        File currentFolder = new File(path);
        if (path.endsWith(JAR)) {
            currentFolder = currentFolder.getParentFile();
        }
        String warPath = null;
        for (File file : currentFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(WAR)) {
                warPath = file.getAbsolutePath();
                break;
            }
        }
        return (warPath != null) ? new WebApp(warPath, CONTEXT_PATH) : null;
    }
}
