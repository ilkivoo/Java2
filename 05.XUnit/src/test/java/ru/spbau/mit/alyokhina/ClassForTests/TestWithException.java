package ru.spbau.mit.alyokhina.ClassForTests;

import ru.spbau.mit.alyokhina.annotation.Test;

import java.io.IOException;

public class TestWithException {

    @Test
    private void test1() {
        throw new NullPointerException();
    }

    @Test(expected = NullPointerException.class)
    private void test2() {
        throw new NullPointerException();
    }

    @Test(expected = IOException.class)
    private void test3() {
        throw new NullPointerException();
    }

    @Test(expected = NullPointerException.class)
    private void test4() {
    }

    @Test
    private void test5() {

    }
}
