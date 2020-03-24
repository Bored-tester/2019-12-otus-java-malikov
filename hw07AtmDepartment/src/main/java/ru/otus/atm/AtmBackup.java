package ru.otus.atm;

import lombok.Getter;
import ru.otus.atm.auth.AtmAuth;
import ru.otus.atm.auth.AtmAuthImpl;
import ru.otus.atm.moneyStorage.MoneyStorage;

@Getter
public class AtmBackup {
    private final MoneyStorage moneyStorage;
    private final AtmAuth atmAuth;

    public AtmBackup(MoneyStorage moneyStorage) {
//      let's assume, I want clean login caches after restore
        this.atmAuth = new AtmAuthImpl();
        this.moneyStorage = moneyStorage.clone();
    }
}
