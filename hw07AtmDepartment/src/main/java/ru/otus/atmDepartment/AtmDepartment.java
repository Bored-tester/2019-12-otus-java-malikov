package ru.otus.atmDepartment;

import ru.otus.atm.AtmListener;
import ru.otus.atm.enums.CcyCode;

import java.util.Map;

public interface AtmDepartment {

    Map<CcyCode, Double> getAtmsTotalBalance();

    void addAtm(AtmListener atmListener);

    void removeAtm(AtmListener atmListener);

    void restoreAtmsToBackups();

}
