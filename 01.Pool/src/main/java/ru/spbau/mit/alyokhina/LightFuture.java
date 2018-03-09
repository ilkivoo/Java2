package ru.spbau.mit.alyokhina;

import java.util.function.Function;

/** Interface for  tasks accepted for execution */
public interface LightFuture<T> {
    /** if task was calculated then return true, else - false */
    boolean isReady();

    /**
     * Calculate supplier
     * @return the value obtained
     * @throws LightExecutionException if in calculating supplier of the LightFuture we caught an exception
     */
    T get() throws LightExecutionException;

    /**
     * Accepts an object of type Function that can be applied to the result of this task T and returns a new task E accepted for execution
     * @return new task type of E
     */
    <E> LightFuture<E> thenApply(Function<T, E> function);
}
