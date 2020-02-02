package ru.otus.checker.testClasses;

public class TestSon extends TestRoyalty{

    private Integer age;
    private TestFather father;

    public TestSon(String name, TestFather father, int age) {
        super(Titles.PRINCE, name, father.getSurname());
        if (age < 0) throw new IllegalArgumentException("Person's age can't be negative");
        this.father = father;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public String toString(){
        return String.format("\nHis Highness %s %s %s %d years old", getTitle(), getName(), getSurname(), age);
    }

    public void setFather(TestFather father) {
        this.father = father;
    }
}
