package ru.otus.demo;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestClass4Tests2Pass2Fail2Before2After {

    @Before
    public void beforeTest1() {
        System.out.println("Before test 1 section");
    }

    @Before
    public void beforeTest2() {
        System.out.println("Before test 2 section");
    }

    @After
    public void afterTest1() {
        System.out.println("After test 1 section");
    }

    @After
    public void afterTest2() {
        System.out.println("After test 2 section");
    }

    @Test
    public void goodTest1() {
        System.out.println("All is good. Test1 passed.");
    }

    @Test
    public void goodTest2() {
        System.out.println("All is good. Test2 passed.");
    }

    @Test
    public void badTest3() {
        System.out.println("All is bad. Test3 failed.");
        throw new IllegalArgumentException("test3 has incorrect argument somewhere");
    }

    @Test
    public void badTest4() {
        System.out.println("All is bad. Test4 failed.");
        assertThat(1)
                .as("This will always fail")
                .isZero();
    }
}
