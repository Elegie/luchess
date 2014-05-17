package io.elegie.luchess.core.indexing.workflow;

/**
 * This class is used to configure the {@link IndexingSession} threading model,
 * i.e. the number of threads to be used, as well as the timeout after which the
 * session must interrupt its processing.
 */
@SuppressWarnings("javadoc")
public class ThreadingProfile {

    private int numberOfThreads;
    private int timeoutSeconds;

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

}
