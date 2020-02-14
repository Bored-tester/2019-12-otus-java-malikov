package ru.otus.checker.testClasses;

import ru.otus.task.DIYArrayList;

import java.util.Collections;
import java.util.Comparator;

public class TestFather extends TestRoyalty {
    private DIYArrayList<TestSon> sons;

    public TestFather(String name, String surname) {
        super(Titles.KING, name, surname);
        this.sons = new DIYArrayList<>();
    }

    public void adoptSons(TestFather fromFather) {
        this.sons = new DIYArrayList<>(fromFather.sons.size());
        // I know that it makes more sense just to assign all sons, but I want to check copy
        Collections.copy(this.sons, fromFather.sons);
        this.sons.forEach(son -> son.setFather(this));
        fromFather.sons = new DIYArrayList<>();
    }

    public DIYArrayList<TestSon> getSons() {
        DIYArrayList<TestSon> copyOfSons = new DIYArrayList<>(this.sons.size());
        Collections.copy(copyOfSons, this.sons);
        return copyOfSons;
    }

    public void acknowledgeSon(TestSon son) {
        this.sons.add(son);
        Collections.sort(this.sons, Comparator.comparingInt(TestSon::getAge));
    }

    public void acknowledgeSons(TestSon... sons) {
        Collections.addAll(this.sons, sons);
        Collections.sort(this.sons, Comparator.comparingInt(TestSon::getAge));
    }

    public String toString() {
        return String.format("\nHis Eminence %s %s %s\n and his sons: %s", getTitle(), getName(), getSurname(), sons);
    }
}
