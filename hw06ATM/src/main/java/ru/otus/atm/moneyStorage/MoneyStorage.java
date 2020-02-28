package ru.otus.atm.moneyStorage;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.Ccy;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmErrorException;
import ru.otus.atm.utils.Banknote;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MoneyStorage {
    private Map<CcyCode, CcyStorage> ccyTrays;

    public MoneyStorage() {
        ccyTrays = Arrays.stream(Ccy.values()).collect(Collectors.toMap(ccy -> ccy.getCcyCode(), ccy -> new CcyStorage(ccy.getNominals())));
    }

    public void deleteStorageForCcy(CcyCode ccyCode) {
        ccyTrays.remove(ccyCode);
    }

    public void withdraw(CcyCode ccyCode, Double amount) {
        ccyTrays.get(ccyCode).withdrawMoney(amount);
    }

    public void insert(Banknote banknote) {
        CcyCode ccyCode = banknote.getCcyCode();
        int nominal = banknote.getNominal();
        if (!Ccy.checkNominalSupported(ccyCode, nominal))
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Nominal %s is not supported for the ccy %s", nominal, ccyCode));
        ccyTrays.get(ccyCode).putSingleNote(nominal);
    }

    private Double getTotal(CcyCode ccyCode) {
        return ccyTrays.get(ccyCode).getTotalCcyBalance();
    }

    public void installTray(CcyCode ccyCode, int nominal, int notesCount) {
        if (!Ccy.checkNominalSupported(ccyCode, nominal))
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Nominal %s is not supported for the ccy %s", nominal, ccyCode));
        ccyTrays.get(ccyCode).addTrayForNominal(nominal, notesCount);
    }

    public void printOutMoneyStorageState() {
        ccyTrays.forEach((ccy, traySet) -> {
            System.out.println(String.format("Ccy %s, balance %f:\n", ccy, getTotal(ccy)));
            traySet.printOutNominalStorageState();
        });
    }
}
