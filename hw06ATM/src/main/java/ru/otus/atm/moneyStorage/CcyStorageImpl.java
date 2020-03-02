package ru.otus.atm.moneyStorage;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmErrorException;

import java.util.*;
import java.util.stream.Collectors;

class CcyStorageImpl implements CcyStorage {
    private Map<Integer, NominalStorage> traySet;
    private final List<Integer> sortedNominals;

    CcyStorageImpl(List<Integer> nominals) {
        nominals.sort(Collections.reverseOrder());
        sortedNominals = nominals;
        traySet = sortedNominals.stream().collect(Collectors.toMap(nominal -> nominal, nominal -> {
            NominalStorage nominalStorage = new NominalStorageImpl(nominal);
            nominalStorage.addEmptyTray();
            return nominalStorage;
        }));
    }

    @Override
    public void addTrayForNominal(int nominal, int notesCount) {
        if (!traySet.containsKey(nominal)) traySet.put(nominal, new NominalStorageImpl(nominal));
        traySet.get(nominal).addTray(new MoneyTrayImpl(notesCount, notesCount));
    }

    @Override
    public Double getTotalCcyBalance() {
        return traySet.values().stream().map(NominalStorage::getMoneyTotal).reduce(0.0, Double::sum);
    }

    private Optional<Integer> getMinimalNominalPresent() {
        return traySet.values().stream()
                .filter(nominalStorage -> nominalStorage.getTotalNotesCount() > 0)
                .map(NominalStorage::getNominal)
                .reduce(Integer::min);
    }

    @Override
    public void putSingleNote(Integer nominal) {
        NominalStorage nominalStorage = traySet.get(nominal);
        if (nominalStorage == null)
            throw new AtmErrorException(AtmErrorType.INTERNAL_ERROR, String.format("No trays are reserved for such nominal %s.", nominal));
        nominalStorage.putSingleNote();
    }

    @Override
    public void withdrawMoney(double amountToWithdraw) {
        if (amountToWithdraw <= 0)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Please withdraw only positive amount. You are trying to withdraw %f", amountToWithdraw));

        if (amountToWithdraw > getTotalCcyBalance())
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_ATM_FUNDS, String.format("Trying to withdraw more money then the ATM has!\n" +
                            "Attempting to withdraw: %f\nAvailable: %f",
                    amountToWithdraw, getTotalCcyBalance()));

        int minimalNoteNominal = getMinimalNominalPresent().get();
        if (amountToWithdraw % minimalNoteNominal > 0)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Amount must divide by %d without leftover. " +
                    "Please specify another sum. You are trying to withdraw %f", minimalNoteNominal, amountToWithdraw));

        Map<Integer, Long> nominalAmountsToWithdraw = breakAmountIntoNominals(amountToWithdraw);
        nominalAmountsToWithdraw.forEach((nominal, numberOfNotes) -> traySet.get(nominal).withdrawMoney(numberOfNotes));
    }

    private Map<Integer, Long> breakAmountIntoNominals(Double amountToBreak) {
        Map<Integer, Long> nominalAmountsToWithdraw = new HashMap<>();
        for (Integer nominal : sortedNominals) {
            long notesShouldBeWithdrawn = amountToBreak.longValue() / nominal;
            int availableNotes = traySet.get(nominal).getTotalNotesCount();
            long notesToWithdraw = Math.min(notesShouldBeWithdrawn, availableNotes);
            if (notesToWithdraw > 0) {
                amountToBreak -= notesToWithdraw * nominal;
                nominalAmountsToWithdraw.put(nominal, notesToWithdraw);
            }
            if (amountToBreak == 0.0) return nominalAmountsToWithdraw;
        }
        throw new AtmErrorException(AtmErrorType.INSUFFICIENT_ATM_FUNDS, String.format("Failed to break requested amount into available banknotes!\n" +
                        "Leftover amount: %f\nin banknotes: %s",
                amountToBreak, nominalAmountsToWithdraw));
    }

    @Override
    public void printOutNominalStorageState() {
        traySet.forEach((nominal, traySet) -> {
            if (traySet.getTotalNotesCount() > 0)
                System.out.println(String.format("Nominal %s, balance: %f, notes count: %d\n", nominal, traySet.getMoneyTotal(), traySet.getTotalNotesCount()));
        });
    }
}
