package ru.otus;

import com.google.common.collect.ImmutableList;

public class HelloOtus {
    private static final ImmutableList<String> INSTRUCTORS =
            ImmutableList.of(
                    "Сергей Петрелевич",
                    "Вадим Тисов",
                    "Стрекалов Павел",
                    "Александр Оруджев",
                    "Вячеслав Лапин",
                    "Виталий Куценко");

    public static void main(String... args) {
        System.out.println("Hello to all of our senseis:\n"
                + String.join(",\n", INSTRUCTORS)
                + "!");
    }
}
