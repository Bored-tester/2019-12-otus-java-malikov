package ru.otus.checker;

import ru.otus.checker.testClasses.TestFather;
import ru.otus.checker.testClasses.TestRoyalty;
import ru.otus.checker.testClasses.TestSon;
import ru.otus.task.DIYArrayList;

import java.util.Collections;
import java.util.Comparator;

public class ArrayListValidator {

    public static void main(String... args) {
        testIntegerDIYArrayList();
        testRoyalty();
    }

    private static void testIntegerDIYArrayList() {

        DIYArrayList diyIntList1 = new <Integer>DIYArrayList();
        diyIntList1.add(3);
        diyIntList1.add(2);
        diyIntList1.add(1);
        System.out.println("initial array1: " + diyIntList1);
        Collections.sort(diyIntList1);
        System.out.println("sorted array1: " + diyIntList1);

        Collections.addAll(diyIntList1, 546, 5, 555);
        System.out.println("add all to array1: " + diyIntList1);

        for (int i = 0; i < (DIYArrayList.DEFAULT_SIZE * 3); i++)
            diyIntList1.add(i);
        System.out.println("add many elements to array1 one by one: " + diyIntList1);

        Collections.addAll(diyIntList1, diyIntList1.toArray());
        System.out.println("add a lot of elements in a batch via addAll to array1: " + diyIntList1);

        DIYArrayList diyIntList2 = new <Integer>DIYArrayList();
        diyIntList2.add(3);
        diyIntList2.add(3);
        diyIntList2.add(89);
        System.out.println("initial array2: " + diyIntList2);

        Collections.copy(diyIntList1, diyIntList2);
        System.out.println("copied array2 into array1: " + diyIntList1);

        Collections.sort(diyIntList1, Integer::compare);
        System.out.println("sorted array1: " + diyIntList1);
    }

    private static void testRoyalty() {
        //check sort and basic flow
        TestFather kingLudwig = new TestFather("Pier", "Ludwig");
        kingLudwig.acknowledgeSon(new TestSon("Ivan", kingLudwig, 2));
        kingLudwig.acknowledgeSon(new TestSon("Petya", kingLudwig, 99));
        kingLudwig.acknowledgeSon(new TestSon("Denis", kingLudwig, 2));
        kingLudwig.acknowledgeSon(new TestSon("Igor", kingLudwig, 1));
        System.out.println(kingLudwig);

        // check copy
        TestFather kingShiva = new TestFather("Barak", "Shiva");
        kingShiva.adoptSons(kingLudwig);
        System.out.println(kingLudwig);
        System.out.println(kingShiva);

        // check addAll and sort
        TestFather kingDaniels = new TestFather("Jack", "Daniels");

        kingDaniels.acknowledgeSons(new TestSon("John", kingDaniels, 22),
                new TestSon("Johnny", kingDaniels, 2),
                new TestSon("Johnathan", kingDaniels, 102),
                new TestSon("Big John", kingDaniels, 1),
                new TestSon("Little John", kingDaniels, 200));
        System.out.println(kingDaniels);

        // check String sort
        DIYArrayList<TestRoyalty> kings = new DIYArrayList<>();
        Collections.addAll(kings, kingDaniels, kingShiva, kingLudwig, new TestSon("Sneaky Pit", kingLudwig, 666));
        Collections.sort(kings, Comparator.comparing(TestRoyalty::getName));
        System.out.println(kings);

    }
}
