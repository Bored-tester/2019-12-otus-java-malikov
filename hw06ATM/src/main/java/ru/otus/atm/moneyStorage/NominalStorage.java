package ru.otus.atm.moneyStorage;

interface NominalStorage {

    int getNominal();

    void addTray(MoneyTray tray);

    void addEmptyTray();

    int getTotalNotesCount();

    double getMoneyTotal();

    void putSingleNote();

    void withdrawMoney(long banknotesToWithdraw);
}
