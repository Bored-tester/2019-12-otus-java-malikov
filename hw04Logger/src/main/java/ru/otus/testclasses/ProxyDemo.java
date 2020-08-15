package ru.otus.testclasses;


/*
    java -javaagent:proxyDemo.jar -jar proxyDemo.jar
*/
public class ProxyDemo {

    public static void main(String[] args) {
        MyClassImpl myClass = new MyClassImpl();
        myClass.secureAccess("Security Param", "banana");
        myClass.secureAccessWithoutLogs("Security Param", "banana");
        myClass.secureAccess(null, "banana");
        myClass.secureAccess(1l, Long.MAX_VALUE);
        myClass.crazySecureAccess(111, "banana", 2.0f, true, (byte) 1, (short) 1, 2, 3.0);

        myClass.bleedForTheBloodGod(myClass);
    }

}
