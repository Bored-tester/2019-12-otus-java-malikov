package ru.otus.atm.utils;

import lombok.Getter;
import ru.otus.atm.enums.AtmErrorType;


public class AtmErrorException extends Error {
    @Getter
    private final AtmErrorType errorType;

    public AtmErrorException(AtmErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    @Override
    public String toString() {
        return this.errorType + "\n" + this.getMessage();
    }
}
