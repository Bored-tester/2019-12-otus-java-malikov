package ru.otus.testRunner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.testRunner.enums.TestStatus;

@Getter
@AllArgsConstructor
class TestResult {
    private TestStatus testStatus;
    private String testMethodName;
    private String errorMessage;
}
