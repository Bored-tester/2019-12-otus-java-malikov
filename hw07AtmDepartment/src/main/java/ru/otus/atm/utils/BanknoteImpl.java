package ru.otus.atm.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.atm.enums.CcyCode;

@Getter
@AllArgsConstructor
public class BanknoteImpl implements Banknote {
    private final CcyCode ccyCode;
    private final Integer nominal;

    @Override
    public Double getAmount() {
        return Double.valueOf(nominal);
    }
}
