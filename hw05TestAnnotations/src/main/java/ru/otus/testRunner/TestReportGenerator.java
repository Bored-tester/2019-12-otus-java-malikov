package ru.otus.testRunner;

import ru.otus.testRunner.enums.TestStatus;

import java.util.List;
import java.util.stream.Collectors;


class TestReportGenerator {

    static void printTestReport(Class testingClass, List<TestResult> testResults) {
        List<TestResult> failedTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.FAILED))
                .collect(Collectors.toList());
        List<TestResult> failedOnCleanUpTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.FAILED_ON_CLEANUP))
                .collect(Collectors.toList());
        List<TestResult> passedTests = testResults.stream()
                .filter(testResult -> testResult.getTestStatus().equals(TestStatus.SUCCESS))
                .collect(Collectors.toList());
        StringBuilder testReport = new StringBuilder(String.format("\n###\nTests were run for the class %s\n", testingClass.getName()));
        testReport.append(String.format("Total: %d\nPassed: %d\nFailed: %d\n", testResults.size(), passedTests.size(), (failedTests.size() + failedOnCleanUpTests.size())));
        testReport.append(generateSuccessTestReport(passedTests));

        if (failedOnCleanUpTests.size() > 0) {
            testReport.append(generateFailedTestReport("Following tests passed but failed during After hook:\n", failedOnCleanUpTests));
        }
        testReport.append(generateFailedTestReport("Failed test names and failure reasons:\n", failedTests));

        System.out.println(testReport);
    }

    private static String generateSuccessTestReport(List<TestResult> passedTests) {
        StringBuilder successTestsSection = new StringBuilder("Successful test names:\n");
        List<String> passedTestsNames = passedTests.stream()
                .map(TestResult::getTestMethodName)
                .collect(Collectors.toList());

        successTestsSection.append(String.join(", ", passedTestsNames));
        successTestsSection.append("\n");
        return successTestsSection.toString();
    }

    private static String generateFailedTestReport(String reportSectionHeader, List<TestResult> failedTests) {
        StringBuilder failedTestsSection = new StringBuilder(reportSectionHeader);
        failedTests.forEach(failedTest ->
                failedTestsSection.append(String.format("Test name: %s\nFailure reason:\n%s\n", failedTest.getTestMethodName(), failedTest.getErrorMessage()))
        );

        return failedTestsSection.toString();
    }


}
