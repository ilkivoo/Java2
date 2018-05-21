package ru.spbau.mit.alyokhina;

import java.util.List;

public class Main {
    public static  void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Expected class name");
            return;
        }
        TestExecutor testExecutor = new TestExecutor(Class.forName(args[0]));
        List<TestResult> results = testExecutor.run();
        int all = 0, success = 0, fail = 0, ignored = 0;
        for (TestResult testResult : results) {
            System.out.print("Class: " + testResult.getClassName() + ", Test: " + testResult.getTestName() + ", Time: " + ((Long)testResult.getTime()).toString());
            all++;
            if (testResult.isFail()) {
                fail++;
                System.out.print(" -- Failed: ");
                if (testResult.getException()!= null) {
                    System.out.println(testResult.getException().getMessage());
                }
                else {
                    System.out.println("Expected exception not received");
                }
            }
            else {
                if (testResult.causeOfIgnoring().equals("")) {
                    success++;
                    System.out.println(" -- Success");
                }
                else {
                    ignored++;
                    System.out.println(" -- Ignored: " + testResult.causeOfIgnoring());
                }
            }
            System.out.println("All: " + ((Integer)all).toString() + ", success: " + ((Integer)success).toString() +
                    ", fail: " + ((Integer)fail).toString() + ", ignored: " + ((Integer)ignored).toString());
        }
    }

}
