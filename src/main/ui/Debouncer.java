package ui;

import java.util.concurrent.*;

// Literally. A class for implementing debounce (shattering event queue)
// I DIDN'T COPY I WROTE MY OWN

// Not Thread safe but Psychosis's GUI is single threaded anyways
// TODO: Use Lock for thread safety
public class Debouncer<T> {
    private int interval = 500;
    private long lastFire = 0;
    private final Callable<T> callback;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> future;

    // EFFECTS: init the Debouncer with an empty executor and lastFire set to past before interval
    // (for first-time execution)
    public Debouncer(Callable<T> callback, int interval) {
        this.callback = callback;
        this.interval = interval;
        this.lastFire = System.currentTimeMillis() - this.interval;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    // EFFECTS: if last fire is away interval, schedule an execution after interval
    // otherwise, cancel the execution and create a new future with new interval
    // no matter what, update lastFire
    // MODIFIES: callback's side effect
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    // Suppress as I need single method synchronized execution and I don't want to create more
    // synchronized methods for obvious reasons.
    public synchronized void fire() {
        if (System.currentTimeMillis() > lastFire + interval) {
            // The last call must have gone out of the interval
            // create new scheduled task
            // synchronized guarantee that the task won't be dirty-wrote
            lastFire = System.currentTimeMillis();
            try {
                if (future == null) {
                    // First time execution
                    this.lastFire = System.currentTimeMillis();
                    future = executor.schedule(() -> {
                        try {
                            callback.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, this.interval, TimeUnit.MILLISECONDS);
                } else {
                    while (!future.isDone()) {
                        // If the previous task is not done, we wait for it to finish before replacing it with our new task
                        // rare case, just wait with while
                        // I don't even think we will get there as we have future.cancel(false)'s
                        // Guarantee on uninterrupted execution and synchronous so this should only be executed
                        // after prev is finalized.
                    }
                    // Now future must have been done
                    this.lastFire = System.currentTimeMillis();
                    future = executor.schedule(() -> {
                        try {
                            callback.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, this.interval, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            this.lastFire = System.currentTimeMillis();
            // In interval call, reset the previous task
            future.cancel(false); // Allow prev complete (to avoid partial write on object)
            future = executor.schedule(() -> {
                try {
                    this.callback.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, this.interval, TimeUnit.MILLISECONDS);
        }
    }
}