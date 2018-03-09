package ru.spbau.mit.alyokhina;

import org.junit.Test;
import java.util.function.Supplier;
import static org.junit.Assert.*;

public class LazyFactoryTest {
    @Test
    public void testCreateLazySingleThreadMode() {
        Lazy<Integer> test = LazyFactory.createLazySingleThreadMode(() -> 5);
        assertEquals((Integer) 5, test.get());
        assertEquals((Integer) 5, test.get());
    }

    @Test
    public void testCreateLazySingleThreadModeIfSupplierGetNull() {
        Lazy<Integer> test = LazyFactory.createLazySingleThreadMode(() -> null);
        assertEquals(null, test.get());
        assertEquals(null, test.get());
    }

    @Test
    public void testCreateLazySingleThreadModeIfSupplierChangeValue() {
        Lazy<Integer> test = LazyFactory.createLazySingleThreadMode(new Supplier<Integer>() {
            private boolean flag = false;

            @Override
            public Integer get() {
                Integer ans = flag ? 5 : 6;
                flag = true;
                return ans;
            }
        });
        assertEquals((Integer) 6, test.get());
        assertEquals((Integer) 6, test.get());
    }

    @Test
    public void testCreateLazyMultiThreadedModeForOneThread() {
        Lazy<Integer> test = LazyFactory.createLazyMultiThreadedMode(() -> 5);
        assertEquals((Integer) 5, test.get());
        assertEquals((Integer) 5, test.get());
    }

    @Test
    public void testCreateLazyMultiThreadedModeForThreads() {
        Lazy<Integer> test = LazyFactory.createLazyMultiThreadedMode(() -> 5);
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> assertEquals((Integer) 5, test.get()));
        }
        for (Thread thread : threads) {
            thread.run();
        }
    }

    @Test
    public void testCreateLazyMultiThreadedModeForThreadsIfSupplierChange() throws Exception {
        Lazy<Integer> test = LazyFactory.createLazyMultiThreadedMode(new Supplier<Integer>() {
            private boolean flag = false;

            @Override
            public Integer get() {
                Integer ans = flag ? 5 : 6;
                flag = true;
                return ans;
            }
        });
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {

            threads[i] = new Thread(() -> assertEquals((Integer) 6, test.get()));
        }
        for (Thread thread : threads) {
            thread.run();
        }
    }

}