package ru.otus.atm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Ccy {
    RUB(CcyCode.RUB, Arrays.asList(50, 100, 200, 500, 1000, 2000, 5000)),
    USD(CcyCode.USD, Arrays.asList(5, 10, 20, 50, 100));
    protected CcyCode ccyCode;
    protected List<Integer> nominals;

    public static boolean checkNominalSupported(CcyCode ccyCode, int nominal) {
        return Arrays.stream(Ccy.values()).anyMatch(ccy ->
                ((ccy.getCcyCode().equals(ccyCode)) && (ccy.getNominals().contains(nominal))));
    }
}
