package ru.otus.demo;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Before;
import ru.otus.testRunner.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestClass0Tests2Before2After {

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

}
