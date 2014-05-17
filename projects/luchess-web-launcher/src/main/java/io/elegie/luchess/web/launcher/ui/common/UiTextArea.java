package io.elegie.luchess.web.launcher.ui.common;

import io.elegie.luchess.web.launcher.ui.UiStyles;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Wrapper for a scrollable JTextArea.
 */
@SuppressWarnings({ "javadoc", "serial" })
public class UiTextArea extends JScrollPane {

    public UiTextArea() {
        super(new JTextArea());
        getContent().setEditable(false);
        getContent().setBackground(UiStyles.getBackgroundColor());
        getContent().setFont(UiStyles.getAreaFont());
        setPreferredSize(UiStyles.getAreaDimension());
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public final JTextArea getContent() {
        return (JTextArea) getViewport().getView();
    }

}
