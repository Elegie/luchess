package io.elegie.luchess.web.launcher;

import io.elegie.luchess.web.launcher.services.impl.AnyWarWebAppService;
import io.elegie.luchess.web.launcher.services.impl.JettyServerService;
import io.elegie.luchess.web.launcher.ui.console.Console;

import javax.swing.SwingUtilities;

/**
 * The main class, run when the jar container is executed.
 */
public final class Launcher {

    private Launcher() {

    }

    /**
     * Creates the console, injecting the required service implementations.
     * 
     * @param args
     *            None expected.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("unused")
            @Override
            public void run() {
                String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                new Console(new JettyServerService(), new AnyWarWebAppService(path));
            }
        });
    }
}
