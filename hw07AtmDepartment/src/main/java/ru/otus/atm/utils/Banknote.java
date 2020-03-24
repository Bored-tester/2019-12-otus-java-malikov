package ru.otus.atm.utils;

import ru.otus.atm.enums.CcyCode;

public interface Banknote {
    CcyCode getCcyCode();

    Integer getNominal();

    Double getAmount();
}
