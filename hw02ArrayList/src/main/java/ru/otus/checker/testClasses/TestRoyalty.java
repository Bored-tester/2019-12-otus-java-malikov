package ru.otus.checker.testClasses;

public class TestRoyalty {

    private String title;
    private String name;
    private String Surname;

    protected TestRoyalty(String title, String name, String surname) {
        this.title = title;
        this.name = name;
        Surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return Surname;
    }
}
