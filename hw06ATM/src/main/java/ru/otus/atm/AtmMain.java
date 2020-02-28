package ru.otus.atm;

import lombok.var;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmResponse;
import ru.otus.atm.utils.Banknote;

import static org.assertj.core.api.Assertions.assertThat;

public class AtmMain {

    public static void main(String... args) {
        var atm = new Atm();
        atmInit(atm);
        unknownNominalTrayTest(atm);
        unknownUserTest(atm);
        poorUserTests(atm);
        richUserTests(atm);

    }

    private static void unknownNominalTrayTest(Atm atm) {
        AtmResponse response = atm.installTray(CcyCode.USD, 49, 1);
        assertThat(response.getErrorType()).isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    private static void unknownUserTest(Atm atm) {
        String unknownUser = "alien";
        assertThat(atm.authenticate(unknownUser))
                .as("no token should be returned if login failed").isNotPresent();
    }

    private static void poorUserTests(Atm atm) {
//      Vasya is a poor guy who has only Rub account with 1000 Rub
        String poorUserLogin = "Vasya";
        String poorUserToken = atm.authenticate(poorUserLogin).get();

//      Check balance tests
        AtmResponse response = atm.getTotal(CcyCode.RUB, poorUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully get his balance")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) response.getValue())
                .as("User balance should be a hardcoded 1000 RUB")
                .isEqualTo(1000.0);

        response = atm.getTotal(CcyCode.USD, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to get balance for a nonexistent account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);

//      money withdrawal tests
        response = atm.withdraw(CcyCode.RUB, 700.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 700 Rub as the ATM only has 550 in small notes.")
                .isEqualTo(AtmErrorType.INSUFFICIENT_ATM_FUNDS);

        response = atm.withdraw(CcyCode.RUB, 5000.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 5000 Rub as his account has only 1000 Rub.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);

        response = atm.withdraw(CcyCode.USD, 100.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 100 Usd as he has no USD account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);

        response = atm.withdraw(CcyCode.RUB, 500.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully withdraw his money")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful withdrawal.")
                .isEqualTo(500.0);

//      insert money test
        response = atm.insert(new Banknote(CcyCode.USD, 100), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 100 Usd as he has no USD account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);

        response = atm.insert(new Banknote(CcyCode.RUB, 101), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 101 Rub as it has invalid nominal.")
                .isEqualTo(AtmErrorType.INCORRECT_INPUT);

        response = atm.insert(new Banknote(CcyCode.RUB, 5000), poorUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully insert money")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful money deposit.")
                .isEqualTo(5500.0);

        atm.insert(new Banknote(CcyCode.RUB, 1000), poorUserToken);
//      there was a preinstalled empty ATM tray for 1000Rub which should now appear in ATM state print out
        atm.printOutAtmState();
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful money deposit.")
                .isEqualTo(6500.0);

//      there should be one empty tray for 2000RUB tray of max size of 50 notes. Let's overload it.
        for (int i = 0; i < 50; i++) {
            atm.insert(new Banknote(CcyCode.RUB, 2000), poorUserToken);
        }
        response = atm.insert(new Banknote(CcyCode.RUB, 2000), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 2000 Rub as there is no available 2000Rub tray.")
                .isEqualTo(AtmErrorType.INSUFFICIENT_STORAGE);
        atm.printOutAtmState();
    }

    private static void richUserTests(Atm atm) {
//      Vasya is a poor guy who has only Rub account with 1000 Rub
        String richUserLogin = "Bruce";
        String richUserToken = atm.authenticate(richUserLogin).get();

//      money withdrawal tests
        AtmResponse response = atm.withdraw(CcyCode.USD, 211160.0, richUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to withdraw whole ATM value in other ccy")
                .isEqualTo(AtmErrorType.OK);
        atm.printOutAtmState();
    }

    private static void atmInit(Atm atm) {
        atm.installTray(CcyCode.RUB, 50, 10);
        atm.installTray(CcyCode.RUB, 50, 1);
        atm.installTray(CcyCode.RUB, 5000, 100);

        atm.installTray(CcyCode.USD, 5, 10);
        atm.installTray(CcyCode.USD, 10, 1111);
        atm.installTray(CcyCode.USD, 100, 1000);
        atm.installTray(CcyCode.USD, 100, 1000);
        atm.printOutAtmState();
    }
}
