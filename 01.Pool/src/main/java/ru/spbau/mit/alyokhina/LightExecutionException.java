package ru.spbau.mit.alyokhina;

/** if in calculating supplier of the LightFuture we caught an exception, we throw LightExecutionException */
public class LightExecutionException extends Exception {
    public LightExecutionException(String msg) {
        super(msg);
    }
}
