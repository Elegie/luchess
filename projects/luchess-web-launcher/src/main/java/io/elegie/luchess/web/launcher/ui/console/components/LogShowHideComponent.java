package io.elegie.luchess.web.launcher.ui.console.components;

import io.elegie.luchess.web.launcher.ui.UiComponent;
import io.elegie.luchess.web.launcher.ui.UiMessages;
import io.elegie.luchess.web.launcher.ui.common.UiLink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Switch button to show / hide the log console.
 */
@SuppressWarnings("javadoc")
public class LogShowHideComponent implements UiComponent {

    private final UiLink link;

    public LogShowHideComponent(final JFrame container, final LogDisplayComponent logDisplayComponent) {
        link = new UiLink(UiMessages.get(UiMessages.DETAIL_HIDE));
        link.setName(LogShowHideComponent.class.getSimpleName());
        link.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean show = !logDisplayComponent.isVisible();
                logDisplayComponent.setVisible(show);
                link.setText(UiMessages.get(show ? UiMessages.DETAIL_HIDE : UiMessages.DETAIL_SHOW));
                container.pack();
            }
        });
        logDisplayComponent.setVisible(true);
    }

    @Override
    public JComponent getComponent() {
        return link;
    }

}
