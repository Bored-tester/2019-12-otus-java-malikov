package ru.otus.atm.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.atm.enums.CcyCode;

@Getter
@AllArgsConstructor
public class Banknote {
    private final CcyCode ccyCode;
    private final Integer nominal;

    public Double getAmount() {
        return Double.valueOf(nominal);
    }
}
