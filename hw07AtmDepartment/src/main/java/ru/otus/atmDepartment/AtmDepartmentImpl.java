package ru.otus.atmDepartment;

import ru.otus.atm.AtmListener;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.AtmResponse;

import java.util.*;
import java.util.stream.Collectors;

public class AtmDepartmentImpl implements AtmDepartment {
    private List<AtmListener> atmListeners;

    public AtmDepartmentImpl() {
        atmListeners = new ArrayList<>();
    }

    @Override
    public Map<CcyCode, Double> getAtmsTotalBalance() {
        Map<CcyCode, Double> result = new HashMap<>();
        List<Map<CcyCode, Double>> atmsTotals = atmListeners.stream()
                .map(AtmDepartmentImpl::getAtmTotalBalance)
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

    private static Optional<Map<CcyCode, Double>> getAtmTotalBalance(AtmListener atmListener) {
        AtmResponse totalResponse = atmListener.getAtmTotal();
        if (totalResponse.getErrorType().equals(AtmErrorType.OK))
            return Optional.of((Map<CcyCode, Double>) totalResponse.getValue());
        System.out.println(String.format("Atm got the following error during totals calculation: %s", totalResponse.getMessage()));
        return Optional.empty();
    }

    @Override
    public void addAtmListener(AtmListener atmListener) {
        atmListeners.add(atmListener);
    }

    @Override
    public void removeAtmListener(AtmListener atmListener) {
        atmListeners.remove(atmListener);
    }

    @Override
    public void restoreAtmsToBackups() {
        atmListeners.forEach(AtmListener::restoreToBackup);
    }

}
