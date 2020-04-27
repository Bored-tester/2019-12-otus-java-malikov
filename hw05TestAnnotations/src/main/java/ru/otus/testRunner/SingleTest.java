package ru.otus.testRunner;

import lombok.AllArgsConstructor;
import ru.otus.testRunner.enums.TestStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@AllArgsConstructor
class SingleTest {
    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private Method testMethod;
    private Object testingClassObject;

    TestResult execute() {
        TestResult testResult = new TestResult(TestStatus.SUCCESS, testMethod.getName(), null);
        try {
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(testingClassObject);
            }
            testMethod.invoke(testingClassObject);
        } catch (InvocationTargetException e) {
            testResult = new TestResult(TestStatus.FAILED, testMethod.getName(), getExceptionAsString(e.getTargetException()));
        } catch (Exception e) {
            testResult = new TestResult(TestStatus.FAILED, testMethod.getName(), getExceptionAsString(e));
        }

        try {
            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(testingClassObject);
            }
        } catch (InvocationTargetException e) {
            if (testResult.getTestStatus().equals(TestStatus.SUCCESS))
                testResult = new TestResult(TestStatus.FAILED_ON_CLEANUP, testMethod.getName(), getExceptionAsString(e.getTargetException()));
        } catch (Exception e) {
            if (testResult.getTestStatus().equals(TestStatus.SUCCESS))
                testResult = new TestResult(TestStatus.FAILED_ON_CLEANUP, testMethod.getName(), getExceptionAsString(e));
        }
        return testResult;
    }

    private String getExceptionAsString(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
