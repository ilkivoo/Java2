package ru.spbau.mit.alyokhina;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Class for create object of Lazy
 * There is a single-threaded and multithreaded version of the creation
 *
 * @param <T> type of Lazy object
 */
public class LazyFactory<T> {
    /**
     * Create Lazy object for single thread
     *
     * @param supplier the Lazy object is created on the basis of calculations (represented by the supplier)
     * @param <T>      type of Lazy object
     * @return Lazy object
     */
    @NotNull
    public static <T> Lazy<T> createLazySingleThreadMode(Supplier<T> supplier) {
        return new LazySingleThreadMode<>(supplier);
    }

    /**
     * Create Lazy object for many threads
     *
     * @param supplier the Lazy object is created on the basis of calculations (represented by the supplier)
     * @param <T>      type of Lazy object
     * @return Lazy object
     */
    @NotNull
    public static <T> Lazy<T> createLazyMultiThreadedMode(Supplier<T> supplier) {
        return new LazyMultiThreadMode<>(supplier);
    }


    /**
     * class implementing interface Lazy for one thread
     *
     * @param <T> type of Lazy object
     */
    private static class LazySingleThreadMode<T> implements Lazy<T> {
        /** The Lazy object is created on the basis of calculations (represented by the supplier) */
        private Supplier<T> supplier;

        /** Value that came after calling the supplier (null - if there was no call) */
        private T ans;

        /** Constructor */
        private LazySingleThreadMode(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * If the value has not yet been calculated, then calculate
         *
         * @return the value that was received
         */
        @Override
        public T get() {
            if (ans == null) {
                ans = supplier.get();
            }
            return ans;
        }
    }

    /**
     * class implementing interface Lazy for many threads
     *
     * @param <T> type of Lazy object
     */
    private static class LazyMultiThreadMode<T> implements Lazy<T> {
        /** The Lazy object is created on the basis of calculations (represented by the supplier) */
        private volatile Supplier<T> supplier;

        /** Value that came after calling the supplier (null - if there was no call) */
        private T ans;

        /** Constructor */
        private LazyMultiThreadMode(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * If the value has not yet been calculated, then calculate
         *
         * @return the value that was received
         */
        @Override
        public T get() {
            synchronized (this) {
                if (ans == null) {
                    ans = supplier.get();
                }
            }
            return ans;
        }
    }
}
