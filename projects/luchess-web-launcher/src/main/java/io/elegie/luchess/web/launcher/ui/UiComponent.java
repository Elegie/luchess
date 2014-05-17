package io.elegie.luchess.web.launcher.ui;

import javax.swing.JComponent;

/**
 * Implemented by all our domain graphical components. This interface lets us
 * grab a Swing component which can then be injected into a view.
 */
public interface UiComponent {

    /**
     * @return The Swing component backing the domain component.
     */
    JComponent getComponent();

}
