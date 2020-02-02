package ru.otus.checker;

import ru.otus.task.DIYArrayList;
import ru.otus.checker.testClasses.TestFather;
import ru.otus.checker.testClasses.TestSon;

import java.util.Collections;
import java.util.Comparator;

public class ArrayListValidator {

    public static void main(String... args) {
        testIntegerDIYArrayList();
        testRoyalty();
    }

    private static void testIntegerDIYArrayList(){

        DIYArrayList diyIntList1 = new <Integer>DIYArrayList();
        diyIntList1.add(3);
        diyIntList1.add(2);
        diyIntList1.add(1);
        System.out.println(diyIntList1);
        Collections.sort(diyIntList1);
        System.out.println(diyIntList1);

        DIYArrayList diyIntList2 = new <Integer>DIYArrayList();
        diyIntList2.add(3);
        diyIntList2.add(3);
        diyIntList2.add(89);
        System.out.println(diyIntList2);


        Collections.addAll(diyIntList1, 546, 5, 555);
        System.out.println(diyIntList1);

        Collections.copy(diyIntList1, diyIntList2);
        System.out.println(diyIntList1);
        System.out.println(diyIntList2);

        Collections.sort(diyIntList1, Integer::compare);
        System.out.println(diyIntList1);
    }

    private static void testRoyalty(){
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
        DIYArrayList<TestFather> kings = new DIYArrayList<>();
        Collections.addAll(kings, kingDaniels, kingShiva, kingLudwig);
        Collections.sort(kings, Comparator.comparing(TestFather::getName));
        System.out.println(kings);

    }
}
