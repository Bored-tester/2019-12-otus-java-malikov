package ru.otus.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmResponse;
import ru.otus.atm.utils.Banknote;

import static org.assertj.core.api.Assertions.assertThat;

class AtmTest {
    private static Atm atm = new Atm();
    private static final String BATMAN_LOGIN = "Bruce";
    private static final String VASYA_LOGIN = "Vasya";

    @BeforeAll
    static void setUp() {
        atm.installTray(CcyCode.RUB, 50, 10);
        atm.installTray(CcyCode.RUB, 50, 1);
        atm.installTray(CcyCode.RUB, 5000, 100);

        atm.installTray(CcyCode.USD, 5, 10);
        atm.installTray(CcyCode.USD, 10, 1111);
        atm.installTray(CcyCode.USD, 100, 1000);
        atm.installTray(CcyCode.USD, 100, 1000);
        atm.printOutAtmState();

        String richUserToken = atm.authenticate(BATMAN_LOGIN).get();

        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();
    }

    @Test
    void unknownNominalTrayTest() {
        AtmResponse response = atm.installTray(CcyCode.USD, 49, 1);
        assertThat(response.getErrorType()).isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    @Test
    void unknownUserTest() {
        String unknownUser = "alien";
        assertThat(atm.authenticate(unknownUser))
                .as("no token should be returned if login failed").isNotPresent();
    }

    @Test
    void CheckBalance() {
//      Batman has 1000000000.0 RUB
        String richUserToken = atm.authenticate(BATMAN_LOGIN).get();
        AtmResponse response = atm.getTotal(CcyCode.RUB, richUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully get his balance")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) response.getValue())
                .as("User balance should be a hardcoded 1000000000 RUB")
                .isEqualTo(1000000000.0);
    }

    @Test
    void CheckBalanceOnUnsupportedCcy() {
        String richUserToken = atm.authenticate(BATMAN_LOGIN).get();
        AtmResponse response = atm.getTotal(CcyCode.JPY, richUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to get balance for a nonexistent account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);
    }

    @Test
    void insertNote() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();
        Double balance = (Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue();
        AtmResponse response = atm.insert(new Banknote(CcyCode.RUB, 5000), poorUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully insert money")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful money deposit.")
                .isEqualTo(balance + 5000);
    }

    @Test
    void insertIncorrectCcy() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        AtmResponse response = atm.insert(new Banknote(CcyCode.USD, 100), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 100 Usd as he has no USD account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);
    }

    @Test
    void insertInvalidNominal() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        AtmResponse response = atm.insert(new Banknote(CcyCode.RUB, 101), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 101 Rub as it has invalid nominal.")
                .isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    @Test
    void insertIntoEmptyTray() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();
        atm.printOutAtmState();

        Double balance = (Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue();
        atm.insert(new Banknote(CcyCode.RUB, 1000), poorUserToken);
//      there was a preinstalled empty ATM tray for 1000Rub which should now appear in ATM state print out
        atm.printOutAtmState();
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful money deposit.")
                .isEqualTo(balance + 1000);
    }

    @Test
    void insertTooMuch() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

//      there should be one empty tray for 2000RUB tray of max size of 50 notes. Let's overload it.
        for (int i = 0; i < 50; i++) {
            atm.insert(new Banknote(CcyCode.RUB, 2000), poorUserToken);
        }
        AtmResponse response = atm.insert(new Banknote(CcyCode.RUB, 2000), poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to insert 2000 Rub as there is no available 2000Rub tray.")
                .isEqualTo(AtmErrorType.INSUFFICIENT_STORAGE);
        atm.printOutAtmState();
    }

    @Test
    void withdrawMoreThenAtmHas() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        AtmResponse response = atm.withdraw(CcyCode.RUB, 700.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 700 Rub as the ATM only has 550 in small notes.")
                .isEqualTo(AtmErrorType.INSUFFICIENT_ATM_FUNDS);
    }

    @Test
    void withdrawMoreThenUserHas() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        Double balance = (Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue();
        AtmResponse response = atm.withdraw(CcyCode.RUB, balance + 5000.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 5000 Rub more then he has on his account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);
    }

    @Test
    void withdrawFromAbsentAccount() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        AtmResponse response = atm.withdraw(CcyCode.USD, 100.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 100 Usd as he has no USD account.")
                .isEqualTo(AtmErrorType.ACCOUNT_PROBLEM);
    }

    @Test
    void withdraw() {
        String poorUserToken = atm.authenticate(VASYA_LOGIN).get();

        Double balance = (Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue();
        AtmResponse response = atm.withdraw(CcyCode.RUB, 500.0, poorUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to successfully withdraw his money")
                .isEqualTo(AtmErrorType.OK);
        assertThat((Double) atm.getTotal(CcyCode.RUB, poorUserToken).getValue())
                .as("Users balance should change after successful withdrawal.")
                .isEqualTo(balance - 500.0);
    }

    @Test
    void withdrawAllNotesOfOneCcy() {
        String richUserToken = atm.authenticate(BATMAN_LOGIN).get();
        atm.printOutAtmState();

        AtmResponse response = atm.withdraw(CcyCode.USD, 211160.0, richUserToken);
        assertThat(response.getErrorType())
                .as("User should be able to withdraw whole ATM value in other ccy")
                .isEqualTo(AtmErrorType.OK);
        atm.printOutAtmState();
    }


}