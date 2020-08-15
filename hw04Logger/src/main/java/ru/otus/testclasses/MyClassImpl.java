package ru.otus.testclasses;


import ru.otus.logger.Log;

public class MyClassImpl {

    @Log
    public void secureAccess(String param, String pararuram) {
        System.out.println("secureAccess pretends to work with params: " + param + " " + pararuram);
        System.out.println(MyClassImpl.class.getName());
    }

    @Log
    public void secureAccess(long param, long pararuram) {
        System.out.println("secureAccess pretends to work with params: " + param + " " + pararuram);
        System.out.println(MyClassImpl.class.getName());
    }

    @Log
    public void crazySecureAccess(int param, String pararuram, float uberChislo, boolean tru,
                                  byte by, short sh, long lo, double doub) {
        System.out.println("crazySecureAccess pretends to work with params: " + param + " " + pararuram
                + " " + uberChislo + " " + tru + " " + by + " " + sh + " " + lo + " " + doub);
        System.out.println(MyClassImpl.class.getName());
    }

    public void secureAccessWithoutLogs(String param, String pararuram) {
        System.out.println("secureAccess pretends to work with params: " + param + " " + pararuram);
        System.out.println(MyClassImpl.class.getName());
    }

    @Log
    public static void bleedForTheBloodGod(MyClassImpl myClass) {
        System.out.println("Doing something with myClass " + myClass);
    }


    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
