package ru.otus.atm.moneyStorage;

interface MoneyTray {
    int getTraySize();

    int getBanknoteCount();

    int getId();

    int getFreeSpace();

    void withdraw(long banknotesNumber);

    void putSingleNote();
}
