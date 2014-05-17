package io.elegie.luchess.web.launcher.ui.console.components;

import io.elegie.luchess.web.launcher.ui.UiComponent;
import io.elegie.luchess.web.launcher.ui.UiMessages;
import io.elegie.luchess.web.launcher.ui.UiStyles;
import io.elegie.luchess.web.launcher.ui.common.UiLabel;

import java.awt.Color;

import javax.swing.JComponent;

/**
 * Component displayed when the web app is not available. Basically an error
 * message.
 */
@SuppressWarnings("javadoc")
public class WebAppNoFoundComponent implements UiComponent {

    private UiLabel label;

    public WebAppNoFoundComponent() {
        String message = UiMessages.get(UiMessages.WEB_APP_MISSING);
        Color color = UiStyles.getTextErrorColor();
        label = new UiLabel(message, color);
        label.setName(WebAppNoFoundComponent.class.getSimpleName());
    }

    @Override
    public JComponent getComponent() {
        return label;
    }

}
