package ru.otus.demo;

import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

public class TestClass2Tests2Pass0Fail2Before0After {

    @Before
    public void beforeTest1() {
        System.out.println("Before test 1 section");
    }

    @Before
    public void beforeTest2() {
        System.out.println("Before test 2 section");
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
