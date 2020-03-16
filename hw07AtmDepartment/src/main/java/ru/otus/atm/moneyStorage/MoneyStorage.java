package ru.otus.atm.moneyStorage;

import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.Banknote;

import java.util.Map;

public interface MoneyStorage {

    void deleteStorageForCcy(CcyCode ccyCode);

    void withdraw(CcyCode ccyCode, Double amount);

    void insert(Banknote banknote);

    void installTray(CcyCode ccyCode, int nominal, int notesCount);

    MoneyStorage clone();

    void printOutMoneyStorageState();

    Map<CcyCode, Double> getMoneyStorageState();
}
