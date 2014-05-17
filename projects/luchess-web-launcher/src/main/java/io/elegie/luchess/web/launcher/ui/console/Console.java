package io.elegie.luchess.web.launcher.ui.console;

import io.elegie.luchess.web.launcher.services.api.ServerService;
import io.elegie.luchess.web.launcher.services.api.WebAppService;
import io.elegie.luchess.web.launcher.ui.UiMessages;
import io.elegie.luchess.web.launcher.ui.UiStyles;
import io.elegie.luchess.web.launcher.ui.common.UiPanel;
import io.elegie.luchess.web.launcher.ui.console.components.LogDisplayComponent;
import io.elegie.luchess.web.launcher.ui.console.components.LogShowHideComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WebAppNoFoundComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WebAppStartStopComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WelcomeComponent;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main screen of our launcher, that lets us manage the web server.
 */
@SuppressWarnings("serial")
public class Console extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(Console.class);

    private ServerService serverService;
    private WebAppService webAppService;

    /**
     * @param serverService
     *            Providing the server to start/stop.
     * @param webAppService
     *            Providing the WebApp to load into the server.
     */
    public Console(ServerService serverService, WebAppService webAppService) {
        super(UiMessages.get(UiMessages.TITLE));
        this.serverService = serverService;
        this.webAppService = webAppService;

        Box box = Box.createVerticalBox();
        addWelcomeComponent(box);
        addSeparator(box);
        addControls(box);
        setUp(box);
    }

    private void addWelcomeComponent(Box box) {
        box.add(new WelcomeComponent().getComponent());
    }

    private void addControls(Box box) {
        boolean hasWebApp = webAppService.getWebApp() != null;
        if (hasWebApp) {
            WebAppStartStopComponent webAppStartStopComponent = new WebAppStartStopComponent(serverService, webAppService);
            LogDisplayComponent logDisplayComponent = new LogDisplayComponent();
            LogShowHideComponent logShowHideComponent = new LogShowHideComponent(this, logDisplayComponent);
            UiPanel switches = new UiPanel(0);
            switches.add(webAppStartStopComponent.getComponent());
            switches.add(logShowHideComponent.getComponent());
            box.add(switches);
            addSeparator(box);
            box.add(logDisplayComponent.getComponent());
        } else {
            box.add(new WebAppNoFoundComponent().getComponent());
        }
    }

    private void addSeparator(Box box) {
        box.add(Box.createRigidArea(new Dimension(0, UiStyles.getDefaultPadding())));
    }

    private void setUp(Box box) {
        UiPanel content = new UiPanel(UiStyles.getDefaultPadding());
        content.add(box, BorderLayout.NORTH);
        add(content);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (serverService.isStarted()) {
                    try {
                        serverService.stop();
                    } catch (Exception e) {
                        String message = "Cannot stop server in shutdown hook.";
                        LOG.error(message, e);
                    }
                }
            }
        }));
    }

}