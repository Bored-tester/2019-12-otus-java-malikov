package ru.otus.atmDepartment;

import ru.otus.atm.Atm;
import ru.otus.atm.enums.CcyCode;

import java.util.Map;
import java.util.Optional;

public interface AtmDepartment {

    Map<CcyCode, Double> getAtmsTotalBalance();

    Optional<Map<CcyCode, Double>> getAtmTotalBalance(int id);

    Atm getAtm(int id);

    int addAtm();

    void restoreAtmsToBackups();

}
