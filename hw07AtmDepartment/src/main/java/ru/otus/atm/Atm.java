package ru.otus.atm;

import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmResponse;
import ru.otus.atm.utils.Banknote;

import java.util.Optional;

public interface Atm {

    Optional<String> authenticate(String login);

    void logout(String token);

    AtmResponse withdraw(CcyCode ccyCode, Double amount, String token);

    AtmResponse insert(Banknote banknote, String token);

    AtmResponse getTotal(CcyCode ccyCode, String token);

    AtmListener getListener();

    AtmResponse installTray(CcyCode ccyCode, int nominal, int notesCount);

    void printOutAtmState();

    AtmResponse backup();

}
