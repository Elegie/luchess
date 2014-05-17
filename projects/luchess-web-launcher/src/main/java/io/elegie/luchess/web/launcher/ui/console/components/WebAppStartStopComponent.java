package io.elegie.luchess.web.launcher.ui.console.components;

import io.elegie.luchess.web.launcher.services.api.ServerService;
import io.elegie.luchess.web.launcher.services.api.WebAppService;
import io.elegie.luchess.web.launcher.ui.UiComponent;
import io.elegie.luchess.web.launcher.ui.UiMessages;
import io.elegie.luchess.web.launcher.ui.UiStyles;
import io.elegie.luchess.web.launcher.ui.common.UiButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Switch button used to start and stop the server.
 */
@SuppressWarnings("javadoc")
public class WebAppStartStopComponent implements UiComponent {

    private static final Logger LOG = LoggerFactory.getLogger(WebAppStartStopComponent.class);

    private ServerService serverService;
    private WebAppService webAppService;
    private UiButton button;

    public WebAppStartStopComponent(ServerService serverService, WebAppService webAppService) {
        this.serverService = serverService;
        this.webAppService = webAppService;
        button = new UiButton(UiMessages.get(UiMessages.SERVER_START));
        button.setName(WebAppStartStopComponent.class.getSimpleName());
        button.addActionListener(new ServerSwitchActionListener());
        button.setPreferredSize(UiStyles.getDefaultButtonDimension());
    }

    @Override
    public JComponent getComponent() {
        return button;
    }

    // ------------------------------------------------------------------------

    private class ServerSwitchActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            button.setEnabled(false);
            if (serverService.isStarted()) {
                stopServer();
            } else if (serverService.isStopped()) {
                startServer();
            }
            button.setEnabled(true);
        }

        private void startServer() {
            try {
                serverService.start(webAppService.getWebApp());
                button.setText(UiMessages.get(UiMessages.SERVER_STOP));
            } catch (Exception e) {
                String message = "Cannot start server (%s).";
                message = String.format(message, e.getMessage());
                LOG.error(message, e);
            }
        }

        private void stopServer() {
            try {
                serverService.stop();
                button.setText(UiMessages.get(UiMessages.SERVER_START));
            } catch (Exception e) {
                String message = "Cannot stop server (%s).";
                message = String.format(message, e.getMessage());
                LOG.error(message, e);
            }
        }
    }

}
