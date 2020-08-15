package ru.otus.demo;

import ru.otus.testRunner.annotations.After;
import ru.otus.testRunner.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestClass2Tests0Pass2Fail0Before2After {

    @After
    public void afterTest1() {
        System.out.println("After test 1 section");
    }

    @After
    public void afterTest2() {
        System.out.println("After test 2 section");
    }

    @Test
    public void badTest3() {
        System.out.println("All is bad. Test3 failed.");
        throw new IllegalArgumentException("badTest3 has incorrect argument somewhere");
    }

    @Test
    public void badTest4() {
        System.out.println("All is bad. Test4 failed.");
        assertThat(1)
                .as("This will always fail")
                .isZero();
    }
}
