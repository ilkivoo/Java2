package ru.spbau.mit.alyokhina;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;

import ru.spbau.mit.alyokhina.ClassForTests.TestWithBeforeAndAfter;
import ru.spbau.mit.alyokhina.ClassForTests.TestWithBeforeClassAndAfterClass;

import static org.junit.Assert.*;

public class TestExecutorTest {

    @Test
    public void testWithBeforeAndAfter() throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        TestExecutor testExecutor = new TestExecutor(Class.forName("ru.spbau.mit.alyokhina.ClassForTests.TestWithBeforeAndAfter"));
        List<TestResult> results = testExecutor.run();
        assertEquals(3, results.size());
        assertEquals(3, TestWithBeforeAndAfter.countBefore);
        assertEquals(2, TestWithBeforeAndAfter.countAfter);
    }

    @Test
    public void testWithBeforeClassAndAfterClass() throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        TestExecutor testExecutor = new TestExecutor(Class.forName("ru.spbau.mit.alyokhina.ClassForTests.TestWithBeforeClassAndAfterClass"));
        List<TestResult> results = testExecutor.run();
        assertEquals(3, results.size());
        assertEquals(1, TestWithBeforeClassAndAfterClass.countBeforeClass);
        assertEquals(1, TestWithBeforeClassAndAfterClass.countAfterClass);

    }

    @Test
    public void testWithException() throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        TestExecutor testExecutor = new TestExecutor(Class.forName("ru.spbau.mit.alyokhina.ClassForTests.TestWithException"));
        List<TestResult> results = testExecutor.run();
        assertEquals(5, results.size());
        results.sort(Comparator.comparing(TestResult::getTestName));

        assertEquals("test1", results.get(0).getTestName());
        assertEquals(true, results.get(0).isFail());
        assertTrue(results.get(0).getException() instanceof NullPointerException);
        assertTrue(results.get(0).causeOfIgnoring().equals(""));

        assertEquals("test2", results.get(1).getTestName());
        assertEquals(false, results.get(1).isFail());
        assertTrue(results.get(1).getException() instanceof NullPointerException);
        assertTrue(results.get(1).causeOfIgnoring().equals(""));

        assertEquals("test3", results.get(2).getTestName());
        assertEquals(true, results.get(2).isFail());
        assertTrue(results.get(2).getException() instanceof NullPointerException);
        assertTrue(results.get(2).causeOfIgnoring().equals(""));

        assertEquals("test4", results.get(3).getTestName());
        assertEquals(true, results.get(3).isFail());
        assertTrue(results.get(3).getException() == null);
        assertTrue(results.get(3).causeOfIgnoring().equals(""));

        assertEquals("test5", results.get(4).getTestName());
        assertEquals(false, results.get(4).isFail());
        assertTrue(results.get(4).getException() == null);
        assertTrue(results.get(4).causeOfIgnoring().equals(""));
    }

    @Test
    public void testWithIgnore() throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        TestExecutor testExecutor = new TestExecutor(Class.forName("ru.spbau.mit.alyokhina.ClassForTests.TestIgnored"));
        List<TestResult> results = testExecutor.run();
        assertEquals(4, results.size());
        results.sort(Comparator.comparing(TestResult::getTestName));

        assertEquals("test1", results.get(0).getTestName());
        assertFalse(results.get(0).causeOfIgnoring().equals(""));

        assertEquals("test2", results.get(1).getTestName());
        assertFalse(results.get(1).causeOfIgnoring().equals(""));

        assertEquals("test3", results.get(2).getTestName());
        assertFalse(results.get(2).causeOfIgnoring().equals(""));

        assertEquals("test4", results.get(3).getTestName());
        assertFalse(results.get(3).causeOfIgnoring().equals(""));


    }

}