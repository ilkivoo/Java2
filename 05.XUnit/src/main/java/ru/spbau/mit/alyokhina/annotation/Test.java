package ru.spbau.mit.alyokhina.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the test.
 * Annotation can have two arguments - expected (for excepion) , ignore - to cancel the start and specify the cause
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    String ignore() default "";

    Class<? extends Throwable> expected() default IgnoredThrowable.class;

    class IgnoredThrowable extends Throwable {
    }
}
