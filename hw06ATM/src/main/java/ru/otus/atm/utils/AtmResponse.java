package ru.otus.atm.utils;

import lombok.Getter;
import ru.otus.atm.enums.AtmErrorType;


@Getter
public class AtmResponse {
    private final AtmErrorType errorType;
    private final String message;
    private final Object value;

    public AtmResponse() {
        this.message = "Success";
        this.errorType = AtmErrorType.OK;
        value = null;
    }

    public AtmResponse(Object value) {
        this.message = "Success";
        this.errorType = AtmErrorType.OK;
        this.value = value;
    }

    public AtmResponse(AtmErrorException error) {
        this.message = error.getMessage();
        this.errorType = error.getErrorType();
        this.value = null;
    }
}
