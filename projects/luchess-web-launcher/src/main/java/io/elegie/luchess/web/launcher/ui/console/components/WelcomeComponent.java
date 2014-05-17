package io.elegie.luchess.web.launcher.ui.console.components;

import io.elegie.luchess.web.launcher.ui.UiComponent;
import io.elegie.luchess.web.launcher.ui.UiMessages;
import io.elegie.luchess.web.launcher.ui.common.UiLabel;

import javax.swing.JComponent;

/**
 * An Hello component, used to greet the user.
 */
@SuppressWarnings("javadoc")
public class WelcomeComponent implements UiComponent {

    private UiLabel label;

    public WelcomeComponent() {
        label = new UiLabel(UiMessages.get(UiMessages.WELCOME));
        label.setName(WelcomeComponent.class.getSimpleName());
    }

    @Override
    public JComponent getComponent() {
        return label;
    }

}
