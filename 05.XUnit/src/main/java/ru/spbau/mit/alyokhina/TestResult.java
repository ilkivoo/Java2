package ru.spbau.mit.alyokhina;

public interface TestResult {
    long getTime();
    String getClassName();
    String getTestName();
    boolean isFail();
    String causeOfIgnoring();
    Exception getException();
}
