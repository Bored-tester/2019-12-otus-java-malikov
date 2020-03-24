package ru.otus.atm;

import ru.otus.atm.auth.AtmAuth;
import ru.otus.atm.auth.AtmAuthImpl;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.moneyStorage.MoneyStorage;
import ru.otus.atm.moneyStorage.MoneyStorageImpl;
import ru.otus.atm.user.UserManagementSystem;
import ru.otus.atm.user.UserManagementSystemImpl;
import ru.otus.atm.utils.AtmErrorException;
import ru.otus.atm.utils.AtmResponse;
import ru.otus.atm.utils.Banknote;

import java.util.Map;
import java.util.Optional;

public class AtmImpl implements Atm, AtmListener {
    private MoneyStorage moneyStorage;
    private final UserManagementSystem userManagementSystem;
    private AtmAuth atmAuth;
    private AtmBackup backup;

    public AtmImpl() {
        moneyStorage = new MoneyStorageImpl();
        userManagementSystem = new UserManagementSystemImpl();
        atmAuth = new AtmAuthImpl();
        backup();
    }

    @Override
    public Optional<String> authenticate(String login) {
        if (userManagementSystem.checkUserExists(login)) {
            return Optional.of(atmAuth.generateToken(login));
        }
        return Optional.empty();
    }

    @Override
    public void logout(String token) {
        atmAuth.deleteToken(token);
    }

    @Override
    public AtmResponse withdraw(CcyCode ccyCode, Double amount, String token) {
        try {
            String login = validateToken(token);
            Double balance = userManagementSystem.getBalance(login, ccyCode);
            if (balance < amount)
                throw new AtmErrorException(AtmErrorType.ACCOUNT_PROBLEM, "Trying to withdraw more that you have. Please check your balance.");
            moneyStorage.withdraw(ccyCode, amount);
            userManagementSystem.updateBalance(login, ccyCode, -amount);
            return new AtmResponse();
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public AtmResponse insert(Banknote banknote, String token) {
        try {
            String login = validateToken(token);
            //to check that user has money in this ccy
            userManagementSystem.getBalance(login, banknote.getCcyCode());
            moneyStorage.insert(banknote);
            userManagementSystem.updateBalance(login, banknote.getCcyCode(), banknote.getAmount());
            return new AtmResponse();
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public AtmResponse getTotal(CcyCode ccyCode, String token) {
        try {
            String login = validateToken(token);
            Double balance = userManagementSystem.getBalance(login, ccyCode);
            System.out.println(String.format("User %s has a balance of %f %s", login, balance, ccyCode));
            return new AtmResponse(balance);
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public AtmResponse getAtmTotal() {
        try {
            Map<CcyCode, Double> balance = moneyStorage.getMoneyStorageState();
            return new AtmResponse(balance);
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    private String validateToken(String token) {
        String login = atmAuth.getLogin(token);
        if (login == null)
            throw new AtmErrorException(AtmErrorType.AUTHENTICATION_ERROR, "Unknown user. Please login first.");
        return login;
    }

    @Override
    public AtmResponse installTray(CcyCode ccyCode, int nominal, int notesCount) {
        try {
            moneyStorage.installTray(ccyCode, nominal, notesCount);
            return new AtmResponse();
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public void printOutAtmState() {
        moneyStorage.printOutMoneyStorageState();
    }

    @Override
    public AtmResponse backup() {
        try {
            this.backup = new AtmBackup(moneyStorage);
            return new AtmResponse();
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public AtmResponse restoreToBackup() {
        try {
            this.atmAuth = backup.getAtmAuth();
            this.moneyStorage = backup.getMoneyStorage();
            backup();
            return new AtmResponse();
        } catch (AtmErrorException e) {
            System.out.println(e.toString());
            return new AtmResponse(e);
        }
    }

    @Override
    public AtmListener getListener() {
        return this;
    }
}
