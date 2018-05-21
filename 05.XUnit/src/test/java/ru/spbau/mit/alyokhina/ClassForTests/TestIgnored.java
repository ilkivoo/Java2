package ru.spbau.mit.alyokhina.ClassForTests;

import ru.spbau.mit.alyokhina.annotation.Test;

import java.io.IOException;

public class TestIgnored {
    static int count = 0;
    @Test(ignore = "I want and I ignore")
    protected void test1() {
        count++;
        throw new NullPointerException();
    }

    @Test(ignore = "And this test I ignore")
    void test2() {
        count++;
        throw new NullPointerException();
    }

    @Test(ignore = "And this", expected = NullPointerException.class)
    private void test3() {
        count++;
        throw new NullPointerException();
    }

    @Test( expected = IOException.class, ignore = "And this I don't like")
    public void test4() {
        count++;
        throw new NullPointerException();
    }

}
