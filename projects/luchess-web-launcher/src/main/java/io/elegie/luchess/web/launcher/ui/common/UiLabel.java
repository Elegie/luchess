package io.elegie.luchess.web.launcher.ui.common;

import io.elegie.luchess.web.launcher.ui.UiStyles;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Wrapper for a JLabel.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class UiLabel extends JLabel {

    public UiLabel(String message) {
        this(message, UiStyles.getTextColor());
    }

    public UiLabel(String message, Color color) {
        super(message);
        setForeground(color);
        setFont(UiStyles.getTextFont());
    }
}
