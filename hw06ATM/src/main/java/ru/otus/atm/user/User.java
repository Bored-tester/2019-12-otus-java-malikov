package ru.otus.atm.user;

import lombok.Getter;
import ru.otus.atm.enums.CcyCode;

import java.util.ArrayList;
import java.util.List;

@Getter
class User {
    private final String login;
    private List<Account> accounts;

    User(String login) {
        this.login = login;
        accounts = new ArrayList<>();
    }

    void addAccount(CcyCode ccyCode, Double amount) {
        accounts.add(new Account(ccyCode, amount));
    }
}
