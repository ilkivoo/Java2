package ru.spbau.mit.alyokhina;

import org.junit.Test;

import static org.junit.Assert.*;

public class PoolTest {
    @Test
    public void testConstructor() {
        Pool threadPools = new Pool(5);
        for (int i = 0; i < 5; i++) {
            final int j = i;
            threadPools.add(() -> j);
        }
    }

    /**
     * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
     */
    @Test
    public void testWithOneThread() throws LightExecutionException {
        Pool threadPools = new Pool(1);
        LightFuture<Integer> task = threadPools.add(() -> 1408);
        assertEquals((Integer) 1408, task.get());
    }


    /**
     * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
     */
    @Test
    public void testAdd() throws LightExecutionException {
        Pool threadPools = new Pool(15);
        LightFuture<Integer> tasks[] = new LightFuture[20];
        for (int i = 0; i < 20; i++) {
            final int j = i;
            tasks[i] = threadPools.add(() -> j);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals((Integer) i, tasks[i].get());
        }

    }

    @Test(expected = LightExecutionException.class)
    public void testLightExecutionException() throws LightExecutionException {
        Pool threadPools = new Pool(15);
        LightFuture<Integer> tasks[] = new LightFuture[5];
        for (int i = 0; i < 5; i++) {
            tasks[i] = threadPools.add(() -> {
                throw new RuntimeException("");
            });
        }
        for (int i = 0; i < 5; i++) {
            tasks[i].get();
        }
    }

    @Test
    public void testShutdown() {
        Pool threadPools = new Pool(15);
        LightFuture<Integer> tasks[] = new LightFuture[20];
        for (int i = 0; i < 20; i++) {
            final int j = i;
            tasks[i] = threadPools.add(() -> j);
        }
        threadPools.shutdown();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        Thread[] threads = threadPools.getThreads();
        for (int i = 0; i < threads.length; i++) {
            assertEquals(false, threads[i].isAlive());
        }
    }

    /**
     * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
     */
    @Test
    public void tesThenApplyOnce() throws LightExecutionException {
        Pool threadPools = new Pool(15);
        LightFuture<Integer> task = threadPools.add(() -> 5).thenApply(x -> x * 2);
        assertEquals((Integer) 10, task.get());
    }

    /**
     * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
     */
    @Test
    public void tesThenApply() throws LightExecutionException {
        Pool threadPools = new Pool(15);
        LightFuture<Integer> task = threadPools.add(() -> 5).thenApply(x -> x * 2).thenApply(x -> x + 4);
        assertEquals((Integer) 14, task.get());
    }

}