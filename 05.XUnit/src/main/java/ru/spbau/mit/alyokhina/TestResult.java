package ru.spbau.mit.alyokhina;

/** Interface for information about passing the test */
public interface TestResult {
    /** Time of the test */
    long getTime();

    /** The name of the class in which the test was called */
    String getClassName();

    /** The test name */
    String getTestName();

    /** Test failure */
    boolean isFail();

    /** The reason for which the test was ignored */
    String causeOfIgnoring();

    /** The exception that was thrown by the test */
    Exception getException();
}
