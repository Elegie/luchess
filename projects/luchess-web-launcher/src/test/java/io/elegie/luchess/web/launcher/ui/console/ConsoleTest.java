package io.elegie.luchess.web.launcher.ui.console;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.elegie.luchess.web.launcher.services.api.WebAppService;
import io.elegie.luchess.web.launcher.ui.console.components.LogDisplayComponent;
import io.elegie.luchess.web.launcher.ui.console.components.LogShowHideComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WebAppNoFoundComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WebAppStartStopComponent;
import io.elegie.luchess.web.launcher.ui.console.components.WelcomeComponent;
import io.elegie.luchess.web.launcher.ui.console.helpers.ServerServiceHelper;
import io.elegie.luchess.web.launcher.ui.console.helpers.WebAppServiceHelper;

import javax.swing.JTextArea;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JScrollPaneFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ConsoleTest {

    private ServerServiceHelper serverService;
    private Robot robot;

    @Before
    public void setUp() {
        serverService = new ServerServiceHelper();
        robot = BasicRobot.robotWithCurrentAwtHierarchy();
    }

    @After
    public void tearDown() {
        robot.cleanUp();
    }

    @Test
    public void testJarMissing() {
        FrameFixture console = createConsole(false);
        testHasLabel(createConsole(false), WebAppNoFoundComponent.class.getSimpleName());
        try {
            console.label(WelcomeComponent.class.getSimpleName());
            console.label(WebAppNoFoundComponent.class.getSimpleName());
        } catch (ComponentLookupException e) {
            fail("Required components are missing.");
        }
    }

    @Test
    public void testShowHideLog() {
        FrameFixture console = createConsole(true);
        try {
            JButtonFixture showHideButton = console.button(LogShowHideComponent.class.getSimpleName());
            try {
                console.scrollPane(LogDisplayComponent.class.getSimpleName());
                showHideButton.click();
                try {
                    console.scrollPane(LogDisplayComponent.class.getSimpleName());
                    fail("ShowHide scroll pane should now be hidden.");
                } catch (ComponentLookupException e3) {
                    showHideButton.click();
                    try {
                        console.scrollPane(LogDisplayComponent.class.getSimpleName());
                        assertTrue("Successfully toggling visibility", true);
                    } catch (ComponentLookupException e4) {
                        fail("ShowHide scroll pane should now be visible.");
                    }
                }
            } catch (ComponentLookupException e2) {
                fail("ShowHide scroll pane should not be hidden at first.");
            }
        } catch (ComponentLookupException e1) {
            fail("ShowHide button is missing.");
        }
    }

    @Test
    public void testStartStopServer() {
        FrameFixture console = createConsole(true);
        JButtonFixture startStopButton = console.button(WebAppStartStopComponent.class.getSimpleName());
        JScrollPaneFixture logViewContainer = console.scrollPane(LogDisplayComponent.class.getSimpleName());
        JTextArea logViewArea = (JTextArea) logViewContainer.component().getViewport().getView();
        assertFalse(serverService.isStarted());
        assertTrue(serverService.isStopped());
        assertTrue(logViewArea.getText().isEmpty());
        startStopButton.click();
        assertTrue(serverService.isStarted());
        assertFalse(serverService.isStopped());
        assertTrue(logViewArea.getText().contains(ServerServiceHelper.STARTED));
        assertTrue(logViewArea.getText().contains(WebAppServiceHelper.CONTEXT_PATH));
        startStopButton.click();
        assertFalse(serverService.isStarted());
        assertTrue(serverService.isStopped());
        assertTrue(logViewArea.getText().contains(ServerServiceHelper.STOPPED));

        serverService.setExceptionOnStart(true);
        String textStopped = startStopButton.component().getText();
        startStopButton.click();
        assertFalse(serverService.isStarted());
        assertTrue(serverService.isStopped());
        assertEquals(textStopped, startStopButton.component().getText());
        serverService.setExceptionOnStart(false);

        serverService.setExceptionOnStop(true);
        startStopButton.click();
        String textStarted = startStopButton.component().getText();
        startStopButton.click();
        assertTrue(serverService.isStarted());
        assertFalse(serverService.isStopped());
        assertEquals(textStarted, startStopButton.component().getText());
        serverService.setExceptionOnStop(false);
        startStopButton.click();
        assertEquals(textStopped, startStopButton.component().getText());
    }

    private FrameFixture createConsole(boolean appFound) {
        WebAppService webAppService = new WebAppServiceHelper(appFound);
        return new FrameFixture(robot, new Console(serverService, webAppService));
    }

    private void testHasLabel(FrameFixture frame, String labelName) {
        try {
            frame.label(labelName);
        } catch (ComponentLookupException e) {
            fail("The label (" + labelName + ") was not found in the frame.");
        }
    }

}
