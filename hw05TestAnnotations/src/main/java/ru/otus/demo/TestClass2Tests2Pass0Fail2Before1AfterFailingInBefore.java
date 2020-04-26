package ru.otus.demo;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

public class TestClass2Tests2Pass0Fail2Before1AfterFailingInBefore {

    @Before
    public void beforeTest1() {
        System.out.println("Before test 1 section");
    }

    @Before
    public void beforeTest2() {
        System.out.println("Before test 2 section that fails");
        throw new IllegalArgumentException("beforeTest2 has incorrect argument somewhere");
    }

    @After
    public void afterTest1() {
        System.out.println("After test 1 section");
    }

    @Test
    public void goodTest1() {
        System.out.println("All is good. Test1 passed.");
    }

    @Test
    public void goodTest2() {
        System.out.println("All is good. Test2 passed.");
    }
}
