package ru.otus.atm.moneyStorage;

import lombok.Getter;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmErrorException;

import java.util.ArrayList;
import java.util.List;

class NominalStorageImpl implements NominalStorage {
    private List<MoneyTray> traySet;
    @Getter
    private final int nominal;

    NominalStorageImpl(Integer nominal) {
        this.nominal = nominal;
        traySet = new ArrayList<>();
    }

    @Override
    public void addTray(MoneyTray tray) {
        traySet.add(tray);
    }

    @Override
    public void addEmptyTray() {
        traySet.add(new MoneyTrayImpl());
    }

    @Override
    public int getTotalNotesCount() {
        return traySet.stream().map(MoneyTray::getBanknoteCount).reduce(0, Integer::sum);
    }

    @Override
    public double getMoneyTotal() {
        return getTotalNotesCount() * nominal;
    }

    private int getFreeSpace() {
        return traySet.stream().map(MoneyTray::getFreeSpace).reduce(0, Integer::sum);
    }

    @Override
    public void putSingleNote() {
        int totalAvailableSpace = getFreeSpace();
        if (totalAvailableSpace == 0)
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_STORAGE, "Trying to put more money then the trays can hold! Trays are full");
        for (MoneyTray tray : traySet) {
            int availableSpace = tray.getFreeSpace();
            if (availableSpace > 0) {
                tray.putSingleNote();
                break;
            }
        }
    }

    @Override
    public void withdrawMoney(long banknotesToWithdraw) {
        if (banknotesToWithdraw <= 0)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Please withdraw only positive number of notes. You are trying to withdraw %d", banknotesToWithdraw));
        int totalAvailableBills = getTotalNotesCount();
        if (banknotesToWithdraw > totalAvailableBills)
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_ATM_FUNDS, String.format("Trying to withdraw more money then the trays hold!\n" +
                            "Attempting to withdraw: %d\nAvailable: %d",
                    banknotesToWithdraw, totalAvailableBills));
        for (MoneyTray tray : traySet) {
            int availableBills = tray.getBanknoteCount();
            if (availableBills > 0) {
                long amountToWithdraw = Math.min(availableBills, banknotesToWithdraw);
                tray.withdraw(amountToWithdraw);
                banknotesToWithdraw -= amountToWithdraw;
            }
            if (banknotesToWithdraw == 0) break;
        }
    }
}
