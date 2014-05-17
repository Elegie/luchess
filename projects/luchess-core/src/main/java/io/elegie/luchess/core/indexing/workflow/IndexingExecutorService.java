package io.elegie.luchess.core.indexing.workflow;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This executor service works like a fixed thread pool executor, except that it
 * cancels all tasks if any of them reports an exception.
 */
public class IndexingExecutorService extends ThreadPoolExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(IndexingExecutorService.class);

    private boolean containsException = false;

    /**
     * @param nThreads
     *            The number of threads of the fixed pool.
     */
    public IndexingExecutorService(int nThreads) {
        super(nThreads, nThreads, 0L, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        Throwable taskException = throwable;
        if (taskException == null && runnable instanceof Future<?>) {
            try {
                ((Future<?>) runnable).get();
            } catch (CancellationException | InterruptedException e) {
                taskException = e;
            } catch (ExecutionException e) {
                taskException = e;
                Throwable cause = e.getCause();
                if (cause != null) {
                    taskException = cause;
                }
            }
        }
        if (taskException != null) {
            String message = "Thread %s: aborting all indexing tasks.";
            message = String.format(message, Thread.currentThread());
            LOG.error(message, taskException);
            shutdownNow();
            containsException = true;
        }
    }

    /**
     * @return True if one of the task has terminated with an exception.
     */
    public boolean containsException() {
        return containsException;
    }
}
