package ru.spbau.mit.alyokhina;

import ru.spbau.mit.alyokhina.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestExecutor {
    private Object instance;
    private String testClassName;
    private List<Method> before = new ArrayList<>();
    private List<Method> beforeClass = new ArrayList<>();
    private List<Method> after = new ArrayList<>();
    private List<Method> afterClass = new ArrayList<>();
    private List<Method> tests = new ArrayList<>();


    public TestExecutor(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        getMethods(clazz);
        instance = clazz.newInstance();
        testClassName = clazz.getName();
    }


    public List<TestResult> run() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results = new ArrayList<>();
        if (beforeClass.size() != 0) {
            beforeClass.get(0).invoke(instance);
        }

        for (Method method : tests) {
            method.setAccessible(true);
            results.add(invoke(method));
        }

        if (afterClass.size() != 0) {
            afterClass.get(0).invoke(instance);
        }

        return results;
    }


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


    private TestResult invoke(final Method method) throws IllegalAccessException {
        final Test testAnnotation = method.getAnnotation(Test.class);

        if (!testAnnotation.ignore().equals("")) {
            return getResult(0, testClassName, method.getName(), false, testAnnotation.ignore(), null);
        }

        long startTimer = System.currentTimeMillis();
        Exception exception = null;
        try {
            if (before.size() != 0) {
                before.get(0).invoke(instance);
            }
            method.invoke(instance);

            if (after.size() != 0) {
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

    private void getMethods(Class<?> testClazz) {
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
                        //ERROR
                        System.out.println("ERROR");
                    }
                    lists.get(i).add(method);
                    flag = true;
                }
            }
        }
        for (int i = 0; i < lists.size() - 1; i++) {
            if (lists.get(i).size() > 1) {
                //ERROR
                System.out.println("ERROR");
            }
        }
    }


}
