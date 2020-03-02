package ru.otus.atm.user;

import ru.otus.atm.enums.CcyCode;

//test class to emulate separate service
public interface UserManagementSystem {

    boolean checkUserExists(String login);

    Double getBalance(String login, CcyCode ccyCode);

    void updateBalance(String login, CcyCode ccyCode, Double diff);
}
