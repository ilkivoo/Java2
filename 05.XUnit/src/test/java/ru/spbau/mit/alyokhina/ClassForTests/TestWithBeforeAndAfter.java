package ru.spbau.mit.alyokhina.ClassForTests;

import ru.spbau.mit.alyokhina.annotation.*;


public class TestWithBeforeAndAfter {
    public static int countBefore = 0, countAfter = 0;
    @Before
    void before() {
        countBefore++;
    }

    @After
    void after() {
        countAfter++;
    }

    @Test
    void test1() {
        throw new NullPointerException();
    }

    @Test
    void  test2() {
        int x = 1;
    }

    @Test
    void  test3() {
        int x = 1;
    }
}
