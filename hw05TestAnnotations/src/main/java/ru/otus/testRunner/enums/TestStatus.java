package ru.otus.testRunner.enums;

import lombok.Getter;

@Getter
public enum TestStatus {
    SUCCESS,
    FAILED,
    FAILED_ON_CLEANUP;
}
