package io.elegie.luchess.web.client.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class BuildMonitorTest {

    private BuildMonitor monitor;

    @Before
    public void setUp() {
        monitor = new BuildMonitor();
    }

    @Test
    public void testWorkflow() {
        assertEquals(BuildMonitor.Status.CREATED, monitor.getStatus());
        monitor.started();
        assertEquals(BuildMonitor.Status.STARTED, monitor.getStatus());
        monitor.gameAdded();
        assertEquals(1, monitor.getNumGames());
        monitor.finished();
        assertEquals(BuildMonitor.Status.FINISHED, monitor.getStatus());
        monitor.started();
        assertEquals(BuildMonitor.Status.STARTED, monitor.getStatus());
        monitor.failed(null);
        assertEquals(BuildMonitor.Status.FAILED, monitor.getStatus());
        assertTrue(monitor.acknowledgeFailure());
        assertFalse(monitor.acknowledgeFailure());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWhenAlreadyStarted() {
        monitor.started();
        monitor.started();
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishedWhenNotStarted() {
        monitor.finished();
    }

}
