package ru.otus.atm;

import ru.otus.atm.utils.AtmResponse;

public interface AtmListener {

    AtmResponse getAtmTotal();

    AtmResponse restoreToBackup();

}
