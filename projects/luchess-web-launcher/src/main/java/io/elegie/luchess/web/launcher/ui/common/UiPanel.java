package io.elegie.luchess.web.launcher.ui.common;

import io.elegie.luchess.web.launcher.ui.UiStyles;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

/**
 * Wrapper for a Jpanel.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class UiPanel extends JPanel {

    public UiPanel(int padding) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(UiStyles.getBackgroundColor());
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(UiStyles.getBorder(padding));
    }
}
