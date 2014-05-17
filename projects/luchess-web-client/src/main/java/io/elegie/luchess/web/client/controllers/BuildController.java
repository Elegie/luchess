package io.elegie.luchess.web.client.controllers;

import io.elegie.luchess.core.api.BuilderService;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.web.client.ClientContext;
import io.elegie.luchess.web.client.Models;
import io.elegie.luchess.web.client.models.BuildInfo;
import io.elegie.luchess.web.client.models.BuildMonitor;
import io.elegie.luchess.web.framework.payload.Result;
import io.elegie.luchess.web.framework.routing.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a new build process, if no build is already running.
 */
public class BuildController {

    private static final Logger LOG = LoggerFactory.getLogger(BuildController.class);

    /**
     * @param buildInfo
     *            Contains the build options, such as the password.
     * @return The index page, with a build being run in background.
     */
    @Controller
    public synchronized Result build(BuildInfo buildInfo) {
        ClientContext context = ClientContext.INSTANCE;
        BuildMonitor monitor = context.getMonitor();
        if (monitor.getStatus().equals(BuildMonitor.Status.STARTED)) {
            String message = "Build process already running.";
            throw new IllegalStateException(message);
        }

        Result result = new IndexController().index();
        if (!context.hasIndexingPassword() || context.indexingPasswordMatches(buildInfo.getIndexingPassword())) {
            try {
                BuilderService builder = context.getServicesFactory().getBuilderService();
                builder.index(context.getDataSet(), monitor);
            } catch (BuildException e) {
                result.getModel().put(Models.ERROR.getValue(), Models.MSG_ERROR_BUILD.getValue());
                String message = "Build exception occured (%s)";
                message = String.format(message, e.getMessage());
                LOG.error(message, e);
            }
        } else {
            result.getModel().put(Models.ERROR.getValue(), Models.MSG_ERROR_BUILD_PASSWORD.getValue());
        }
        return result;
    }
}
