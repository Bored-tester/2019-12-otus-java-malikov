package ru.otus.atmDepartment;

import ru.otus.atm.Atm;
import ru.otus.atm.AtmImpl;
import ru.otus.atm.AtmListener;
import ru.otus.atm.enums.CcyCode;
import ru.otus.atm.utils.BanknoteImpl;

public class AtmDepartmentTest {
    private static final String BATMAN_LOGIN = "Bruce";

    public static void main(String... args) {
        AtmDepartment atmDepartment = new AtmDepartmentImpl();
        Atm firstAtm = new AtmImpl();
        AtmListener firstAtmListener = firstAtm.getListener();
        atmDepartment.addAtm(firstAtmListener);
        System.out.println(String.format("New atm balance: %s", firstAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("Total atm balance: %s", atmDepartment.getAtmsTotalBalance()));

        Atm secondAtm = new AtmImpl();
        AtmListener secondAtmListener = secondAtm.getListener();
        atmDepartment.addAtm(secondAtmListener);
        System.out.println(String.format("Total atm balance after second atm was added: %s", atmDepartment.getAtmsTotalBalance()));

        initFirstAtm(firstAtm);
        firstAtm.backup();

        initSecondAtm(secondAtm);

        System.out.println(String.format("1st atm balance: %s", firstAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("2nd atm balance: %s", secondAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("\nTotal atm balance: %s\n\n", atmDepartment.getAtmsTotalBalance()));

        atmDepartment.restoreAtmsToBackups();
        System.out.println(String.format("1st atm balance after restore: %s", firstAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("2nd atm balance after restore: %s", secondAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("\nTotal atm balance after restore: %s\n\n", atmDepartment.getAtmsTotalBalance()));

        useAtm(firstAtm);
        System.out.println(String.format("1st atm balance after 10$ withdrawal and 100$ deposit: %s", firstAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("\nTotal atm balance after 10$ withdrawal and 100$ deposit: %s\n\n", atmDepartment.getAtmsTotalBalance()));

        atmDepartment.restoreAtmsToBackups();
        System.out.println(String.format("1st atm balance after restore: %s", firstAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("2nd atm balance after restore: %s", secondAtmListener.getAtmTotal().getValue()));
        System.out.println(String.format("\nTotal atm balance after restore: %s\n\n", atmDepartment.getAtmsTotalBalance()));

        atmDepartment.removeAtm(firstAtmListener);
        System.out.println(String.format("\nTotal atm balance after first Atm was removed from the department: %s\n\n", atmDepartment.getAtmsTotalBalance()));
    }

    static void initFirstAtm(Atm atm) {
        atm.installTray(CcyCode.RUB, 50, 10);
        atm.installTray(CcyCode.RUB, 5000, 100);

        atm.installTray(CcyCode.USD, 5, 10);
        atm.installTray(CcyCode.USD, 10, 1000);
        atm.installTray(CcyCode.USD, 100, 1000);
    }

    static void initSecondAtm(Atm atm) {
        atm.installTray(CcyCode.USD, 10, 1000);
        atm.installTray(CcyCode.USD, 100, 1000);
        atm.installTray(CcyCode.USD, 100, 1000);
    }

    static void useAtm(Atm atm) {
        String richUserToken = atm.authenticate(BATMAN_LOGIN).get();
        atm.withdraw(CcyCode.USD, 10.0, richUserToken);
        atm.insert(new BanknoteImpl(CcyCode.USD, 100), richUserToken);

    }
}
