package io.elegie.luchess.web.framework.presenting.fqnbased;

import io.elegie.luchess.web.framework.presenting.Presenter;
import io.elegie.luchess.web.framework.presenting.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Simple presenter which returns a view object, whose fully qualified class
 * name matches the value of the specified view name.
 * </p>
 * 
 * <p>
 * The view to be created should have a default constructor, with no argument.
 * </p>
 */
public class FQNBasedPresenter implements Presenter {

    private static final Logger LOG = LoggerFactory.getLogger(FQNBasedPresenter.class);

    @Override
    public View resolve(String viewName) {
        if (viewName != null) {
            try {
                return (View) Class.forName(viewName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                String message = "Cannot create view from name: %s";
                message = String.format(message, viewName);
                LOG.error(message, e);
            }
        } else {
            String message = "View name must not be null.";
            LOG.error(message);
        }
        return null;
    }

}
