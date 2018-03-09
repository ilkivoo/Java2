package ru.spbau.mit.alyokhina;

/**
 * Lazy computing interface
 * @param <T> type of returned object
 */
public interface Lazy<T> {
    T get();
}
