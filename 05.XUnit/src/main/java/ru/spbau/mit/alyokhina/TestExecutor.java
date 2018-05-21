package ru.spbau.mit.alyokhina;

import ru.spbau.mit.alyokhina.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** Class for tests run */
public class TestExecutor {
    /** Instance of class  for calling tests */
    private Object instance;

    /** Class name for test runs */
    private String testClassName;

    /** Methods with annotation Before */
    private List<Method> before = new ArrayList<>();

    /** Methods with annotation BeforeClass */
    private List<Method> beforeClass = new ArrayList<>();

    /** Methods with annotation After */
    private List<Method> after = new ArrayList<>();

    /** Methods with annotation AfterClass */
    private List<Method> afterClass = new ArrayList<>();

    /** Methods with annotation Test */
    private List<Method> tests = new ArrayList<>();


    /**
     * Constructor
     * @param clazz class from which tests will be run
     * @throws IllegalAccessException if newInstance threw IllegalAccessException
     * @throws InstantiationException if newInstance threw InstantiationException
     * @throws TestExecutorException if if wrong number methods with annotation
     */
    public TestExecutor(Class<?> clazz) throws IllegalAccessException, InstantiationException, TestExecutorException {
        getMethods(clazz);
        instance = clazz.newInstance();
        testClassName = clazz.getName();
    }


    /**
     * Run tests
     * @return list of results of each test
     * @throws InvocationTargetException if we catch exception from the BeforeClass or the AfterClass
     * @throws IllegalAccessException if invoke threw InvocationTargetException
     */
    public List<TestResult> run() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results = new ArrayList<>();
        if (beforeClass.size() != 0) {
            beforeClass.get(0).setAccessible(true);
            beforeClass.get(0).invoke(instance);
        }

        for (Method method : tests) {
            method.setAccessible(true);
            results.add(invoke(method));
        }

        if (afterClass.size() != 0) {
            afterClass.get(0).setAccessible(true);
            afterClass.get(0).invoke(instance);
        }

        return results;
    }


    /**
     *
     * @param time test run time
     * @param className class name for test
     * @param testName test name
     * @param isFail test failure
     * @param causeOfIgnoring reason for which the test was ignored
     * @param e Exception that was thrown by the test
     * @return information about test in interface TestResult
     */
    private TestResult getResult(final long time, final String className, final String testName,
                                 final boolean isFail, final String causeOfIgnoring, final Exception e) {
        return new TestResult() {
            @Override
            public long getTime() {
                return time;
            }

            @Override
            public String getClassName() {
                return className;
            }

            @Override
            public String getTestName() {
                return testName;
            }

            @Override
            public boolean isFail() {
                return isFail;
            }

            @Override
            public String causeOfIgnoring() {
                return causeOfIgnoring;
            }

            @Override
            public Exception getException() {
                return e;
            }

        };
    }


    /**
     * invoke method
     * @param method method that will be called
     * @return information about passing the test
     * @throws IllegalAccessException if invoke threw IllegalAccessException
     */
    private TestResult invoke(final Method method) throws IllegalAccessException {
        final Test testAnnotation = method.getAnnotation(Test.class);

        if (!testAnnotation.ignore().equals("")) {
            return getResult(0, testClassName, method.getName(), false, testAnnotation.ignore(), null);
        }

        long startTimer = System.currentTimeMillis();
        Exception exception = null;
        try {
            if (before.size() != 0) {
                before.get(0).setAccessible(true);
                before.get(0).invoke(instance);
            }
            method.invoke(instance);

            if (after.size() != 0) {
                after.get(0).setAccessible(true);
                after.get(0).invoke(instance);
            }
        } catch (InvocationTargetException e) {
            exception = (Exception) e.getCause();
        }
        long endTimer = System.currentTimeMillis();

        if ((exception != null && !testAnnotation.expected().isInstance(exception)) ||
                (exception == null && !testAnnotation.expected().equals(Test.IgnoredThrowable.class))) {
            return getResult(endTimer - startTimer, testClassName, method.getName(), true, "", exception);
        }

        return getResult(endTimer - startTimer, testClassName, method.getName(), false, "", exception);

    }

    /**
     * Group methods with annotation in class
     * @param testClazz class for test
     * @throws TestExecutorException if wrong number methods with annotation
     */
    private void getMethods(Class<?> testClazz) throws TestExecutorException {
        Class[] classes = {After.class, AfterClass.class, Before.class, BeforeClass.class, Test.class};
        List<List<Method>> lists = new ArrayList<>();
        lists.add(after);
        lists.add(afterClass);
        lists.add(before);
        lists.add(beforeClass);
        lists.add(tests);
        for (Method method : testClazz.getDeclaredMethods()) {
            boolean flag = false;
            for (int i = 0; i < classes.length; i++) {
                if (method.getAnnotation(classes[i]) != null) {
                    if (flag) {
                        throw new TestExecutorException("too much annotations for method " + method.getName());
                    }
                    lists.get(i).add(method);
                    flag = true;
                }
            }
        }
        for (int i = 0; i < lists.size() - 1; i++) {
            if (lists.get(i).size() > 1) {
                throw new TestExecutorException("too much methods with annotation  " + classes[i].getName());
            }
        }
    }


}
