package ru.spbau.mit.alyokhina;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Task pool with a fixed number of threads
 */
public class Pool {
    /**
     * All created threads
     */
    private Thread threads[];

    /**
     * Accepted tasks
     */
    private final Queue<LightFutureImpl> tasks = new ArrayDeque<>();

    /**
     * Constructor
     *
     * @param n number of threads
     */
    public Pool(int n) {
        threads = new Thread[n];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                LightFutureImpl cur;
                try {
                    while (!Thread.interrupted()) {
                        synchronized (tasks) {
                            while (tasks.isEmpty()) {
                                tasks.wait();
                            }
                            cur = tasks.poll();
                        }
                        cur.calculate();
                    }
                }
                catch (InterruptedException e) {
                }
            }
            );
            threads[i].start();
        }
    }

    /**
     * Shutting down all threads
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /** Return all threads */
    public Thread[] getThreads() {
        return threads;
    }

    /**
     * Add new task and notify threads that are waiting for a new task
     */
    public <E> LightFuture<E> add(Supplier<E> supplier) {
        LightFutureImpl<E> newTask = new LightFutureImpl<>(supplier);
        synchronized (tasks) {
            tasks.add(newTask);
            tasks.notify();
        }
        return newTask;
    }

    /**
     * Class for task implements LightFuture
     */
    private class LightFutureImpl<T> implements LightFuture<T> {
        /**
         * value was calculated
         */
        private boolean ready = false;

        /**
         * Error in calculating the supplier
         */
        private LightExecutionException exception;
        private Supplier<T> supplier;
        /**
         * the value that was received when calculating the supplier
         */
        private T ans = null;

        /**
         * Constructor
         *
         * @param supplier for calculating value
         */
        public LightFutureImpl(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Value was calculated
         *
         * @return true if value was calculated, false - else
         */
        @Override
        public boolean isReady() {
            return ready;
        }

        /**
         * Calculate task
         */
        private synchronized void calculate() {
            if (!ready) {
                try {
                    ans = supplier.get();
                } catch (Exception e) {
                    exception = new LightExecutionException(e.getMessage());
                }
                ready = true;
                notifyAll();
            }
        }

        /**
         * expect calculation if not calculated yet
         *
         * @return result which was calculated
         * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
         */
        public synchronized T get() throws LightExecutionException {
            while (!ready) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new LightExecutionException("Interrupted");
                }
            }
            if (exception != null) {
                throw exception;
            }
            return ans;
        }

        /**
         * Accepts an object of type Function that can be applied to the result of this task T and returns a new task E accepted for execution
         *
         * @return new task type of E
         */
        public <E> LightFuture<E> thenApply(Function<T, E> function) {
            return add(() -> {
                try {
                    return function.apply(get());
                } catch (LightExecutionException e) {
                    throw new RuntimeException(e.getMessage(), e.getCause());
                }
            });
        }
    }
}