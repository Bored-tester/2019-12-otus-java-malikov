package ru.otus.atm.moneyStorage;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.Ccy;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmErrorException;
import ru.otus.atm.utils.Banknote;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MoneyStorageImpl implements MoneyStorage {
    private Map<CcyCode, CcyStorage> ccyTrays;

    public MoneyStorageImpl() {
        ccyTrays = Arrays.stream(Ccy.values()).collect(Collectors.toMap(Ccy::getCcyCode, ccy -> new CcyStorageImpl(ccy.getNominals())));
    }

    @Override
    public void deleteStorageForCcy(CcyCode ccyCode) {
        ccyTrays.remove(ccyCode);
    }

    @Override
    public void withdraw(CcyCode ccyCode, Double amount) {
        ccyTrays.get(ccyCode).withdrawMoney(amount);
    }

    @Override
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

    @Override
    public void installTray(CcyCode ccyCode, int nominal, int notesCount) {
        if (!Ccy.checkNominalSupported(ccyCode, nominal))
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Nominal %s is not supported for the ccy %s", nominal, ccyCode));
        ccyTrays.get(ccyCode).addTrayForNominal(nominal, notesCount);
    }

    @Override
    public void printOutMoneyStorageState() {
        ccyTrays.forEach((ccy, traySet) -> {
            System.out.println(String.format("Ccy %s, balance %f:\n", ccy, getTotal(ccy)));
            traySet.printOutNominalStorageState();
        });
    }
}
