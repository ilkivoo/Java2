package ru.spbau.mit.alyokhina;

/**
 * Exception for Test Executor
 * Throws if wrong number methods with annotation
 */
public class TestExecutorException extends Exception {
    public TestExecutorException(String msg) {
        super(msg);
    }
}
