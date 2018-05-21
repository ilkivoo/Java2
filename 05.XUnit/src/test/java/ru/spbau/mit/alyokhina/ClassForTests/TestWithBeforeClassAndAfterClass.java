package ru.spbau.mit.alyokhina.ClassForTests;

import ru.spbau.mit.alyokhina.annotation.*;

public class TestWithBeforeClassAndAfterClass {
    public static int countBeforeClass = 0, countAfterClass = 0;
    @BeforeClass
    void before() {
        countBeforeClass++;
    }

    @AfterClass
    void after() {
        countAfterClass++;
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
