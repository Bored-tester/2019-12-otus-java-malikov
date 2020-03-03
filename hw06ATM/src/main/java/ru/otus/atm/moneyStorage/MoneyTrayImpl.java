package ru.otus.atm.moneyStorage;

import lombok.Getter;
import ru.otus.atm.drivers.MoneyTrayDriver;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmErrorException;

@Getter
class MoneyTrayImpl implements MoneyTray {
    private final int traySize;
    private int banknoteCount;
    private final int id;

    MoneyTrayImpl() {
        this(0, 50);
    }

    MoneyTrayImpl(int notes, int traySize) {
        if (traySize < notes)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Tray can't held more notes(%d) than it's size(%d)!", notes, traySize));
        id = MoneyTrayDriver.registerTray();
        banknoteCount = notes;
        this.traySize = traySize;
    }

    @Override
    public int getFreeSpace() {
        return traySize - banknoteCount;
    }

    @Override
    public void withdraw(long banknotesNumber) {
        if (banknotesNumber < 0)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Impossible to withdraw negative number of notes: %d.", banknotesNumber));
        if (banknotesNumber > banknoteCount)
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_ATM_FUNDS, String.format("Trying to withdraw more money that is present in the tray!\n" +
                            "Attempting to withdraw: %d\nAvailable for withdrawal: %d",
                    banknotesNumber, banknoteCount));
        MoneyTrayDriver.transferNotesFromTrayToOutput(id, banknotesNumber);
        banknoteCount -= banknotesNumber;

    }

    @Override
    public void putSingleNote() {
        if (banknoteCount == traySize)
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_STORAGE, String.format("Trying to put more money then the tray can hold! Tray is full\n" +
                            "\nTray limit: %d",
                    traySize));
        MoneyTrayDriver.transferNoteFromInputToTray(id);
        banknoteCount++;
    }
}
