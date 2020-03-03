package ru.otus.atm.moneyStorage;

interface CcyStorage {
    void addTrayForNominal(int nominal, int notesCount);

    Double getTotalCcyBalance();

    void putSingleNote(Integer nominal);

    void withdrawMoney(double amountToWithdraw);

    void printOutNominalStorageState();
}
