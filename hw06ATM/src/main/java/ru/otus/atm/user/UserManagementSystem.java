package ru.otus.atm.user;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmErrorException;

import java.util.HashMap;
import java.util.Map;

//test class to emulate separate service
public class UserManagementSystem {
    private Map<String, User> users;

    public UserManagementSystem() {
        users = new HashMap<>();
        User vasya = new User("Vasya");
        vasya.addAccount(CcyCode.RUB, 1000.0);
        users.put(vasya.getLogin(), vasya);

        User batman = new User("Bruce");
        batman.addAccount(CcyCode.RUB, 1000000000.0);
        batman.addAccount(CcyCode.USD, 1000000000.0);
        users.put(batman.getLogin(), batman);
    }

    public boolean checkUserExists(String login) {
        return users.containsKey(login);
    }

    public Double getBalance(String login, CcyCode ccyCode) {
        return users.get(login).getAccounts().stream()
                .filter(acc -> acc.getCcyCode().equals(ccyCode))
                .findFirst()
                .orElseThrow(() -> new AtmErrorException(AtmErrorType.ACCOUNT_PROBLEM, String.format("No account for user %s and ccy %s found", login, ccyCode)))
                .getAmount();
    }

    public void updateBalance(String login, CcyCode ccyCode, Double diff) {
        Account account = users.get(login).getAccounts().stream()
                .filter(acc -> acc.getCcyCode().equals(ccyCode))
                .findFirst().get();
        account.setAmount(account.getAmount() + diff);
    }
}
