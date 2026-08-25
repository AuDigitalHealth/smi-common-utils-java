package au.gov.nehta.common.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a subclass task periodically for a configured sleep interval. The
 * scheduled thread can be stopped and restarted.
 */
public abstract class AbstractProcessScheduler extends Thread {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractProcessScheduler.class);

    /**
     * Sleep interval in milliseconds between task executions.
     */
    private final int interval;

    private final AtomicBoolean stopThread = new AtomicBoolean(false);

    /**
     * @param interval sleep interval in milliseconds
     */
    public AbstractProcessScheduler(int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        runPeriodically();
    }

    /**
     * Runs {@link #performThreadTask()} until {@link #stopTimerThread()} is called
     * or the thread is interrupted.
     */
    public void runPeriodically() {
        try {
            this.stopThread.set(false);
            while (!this.stopThread.get()) {
                Thread.sleep(this.interval);
                performThreadTask();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            String errMsg = "The thread running periodic task,'"
                    + this.getClass().getSimpleName()
                    + "', was interrupted while sleeping.";
            LOGGER.warn(errMsg, ex);
        }
    }

    /**
     * Implement the code to execute on each interval.
     */
    public abstract void performThreadTask();

    /**
     * Stops the scheduled loop. Thread-safe.
     */
    public void stopTimerThread() {
        this.stopThread.set(true);
    }

    /**
     * Clears the stop flag and runs the periodic loop on the calling thread.
     */
    public void startThread() {
        this.stopThread.set(false);
        runPeriodically();
    }

    /**
     * @return true if the stop flag is not set
     */
    public boolean isThreadRunning() {
        return !this.stopThread.get();
    }
}
