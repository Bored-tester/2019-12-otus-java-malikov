package ru.otus.common.model;

import lombok.Getter;

import java.io.Serializable;


@Getter
public class AddressDataSet implements Serializable {
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet() {
    }

    @Override
    public String toString() {
        return street;
    }
}
