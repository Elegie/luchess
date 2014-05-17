package io.elegie.luchess.web.launcher.ui.common;

import io.elegie.luchess.web.launcher.ui.UiStyles;

import java.awt.Cursor;

import javax.swing.JButton;

/**
 * Wrapper for a link (web flavor).
 */
@SuppressWarnings({ "javadoc", "serial" })
public class UiLink extends JButton {

    public UiLink(String message) {
        super(message);
        setFont(UiStyles.getLinkFont());
        setForeground(UiStyles.getLinkTextColor());
        setBackground(UiStyles.getBackgroundColor());
        setBorderPainted(false);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

}
