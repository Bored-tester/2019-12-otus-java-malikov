package ru.otus.atmDepartment;

import ru.otus.atm.AtmListener;
import ru.otus.atm.enums.CcyCode;

import java.util.Map;

public interface AtmDepartment {

    Map<CcyCode, Double> getAtmsTotalBalance();

    void addAtmListener(AtmListener atmListener);

    void removeAtmListener(AtmListener atmListener);

    void restoreAtmsToBackups();

}
