package io.elegie.luchess.web.launcher.ui.common;

import io.elegie.luchess.web.launcher.ui.UiStyles;

import javax.swing.JButton;

/**
 * Wrapper for a JButton.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class UiButton extends JButton {

    public UiButton(String message) {
        super(message);
        setFont(UiStyles.getButtonFont());
        setForeground(UiStyles.getButtonTextColor());
        setBackground(UiStyles.getButtonBackgroundColor());
        setOpaque(false);
        setFocusPainted(false);
    }

}
