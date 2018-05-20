package ru.spbau.mit.alyokhina.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    String ignore() default "";

    Class<? extends Throwable> expected() default IgnoredThrowable.class;

    class IgnoredThrowable extends Throwable {
    }
}
