package ru.otus.atmDepartment;

import ru.otus.atm.Atm;
import ru.otus.atm.AtmImpl;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmResponse;

import java.util.*;
import java.util.stream.Collectors;

public class AtmDepartmentImpl implements AtmDepartment {
    private Map<Integer, Atm> atms;
    private int atmCount;

    public AtmDepartmentImpl() {
        atmCount = 0;
        atms = new HashMap<>();
    }

    @Override
    public Map<CcyCode, Double> getAtmsTotalBalance() {
        Map<CcyCode, Double> result = new HashMap<>();
        List<Map<CcyCode, Double>> atmsTotals = atms.keySet().stream()
                .map(id -> getAtmTotalBalance(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Arrays.stream(CcyCode.values()).forEach(ccy -> {
                    Double ccyTotal = atmsTotals.stream()
                            .filter(atmTotal -> atmTotal.containsKey(ccy))
                            .map(atmTotal -> atmTotal.get(ccy))
                            .reduce(0.0, Double::sum);
                    result.put(ccy, ccyTotal);
                }
        );
        return result;
    }

    @Override
    public Optional<Map<CcyCode, Double>> getAtmTotalBalance(int id) {
        AtmResponse totalResponse = atms.get(id).getAtmTotal();
        if (totalResponse.getErrorType().equals(AtmErrorType.OK))
            return Optional.of((Map<CcyCode, Double>) totalResponse.getValue());
        System.out.println(String.format("Atm %d got the following error during totals calculation: %s", id, totalResponse.getMessage()));
        return Optional.empty();
    }

    @Override
    public Atm getAtm(int id) {
        return atms.get(id);
    }

    @Override
    public int addAtm() {
        atms.put(atmCount, new AtmImpl());
        return atmCount++;
    }

    @Override
    public void restoreAtmsToBackups() {
        atms.values().forEach(Atm::restoreToBackup);
    }

}
