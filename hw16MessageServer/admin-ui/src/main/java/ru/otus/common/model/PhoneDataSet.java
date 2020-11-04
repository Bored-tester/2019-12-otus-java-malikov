package ru.otus.common.model;

import lombok.Getter;

import java.io.Serializable;


@Getter
public class PhoneDataSet implements Serializable {

    private String number;

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public PhoneDataSet() {
    }

    @Override
    public String toString() {
        return number;
    }
}
