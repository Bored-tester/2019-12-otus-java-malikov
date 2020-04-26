package ru.otus.demo;


import ru.otus.testRunner.TestRunner;

public class TestDemo {

    public static void main(String[] args) {
        TestRunner.executeTestsInClass(TestClass4Tests2Pass2Fail2Before2After.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        TestRunner.executeTestsInClass(TestClass2Tests2Pass0Fail2Before0After.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        TestRunner.executeTestsInClass(TestClass2Tests0Pass2Fail0Before2After.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        TestRunner.executeTestsInClass(TestClass2Tests2Pass0Fail2Before1AfterFailingInBefore.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        TestRunner.executeTestsInClass(TestClass2Tests2Pass0Fail1Before2AfterFailingInAfter.class);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        TestRunner.executeTestsInClass(TestClass0Tests2Before2After.class);
    }

}
