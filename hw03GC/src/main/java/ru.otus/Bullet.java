package ru.otus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class Bullet {
    private BigInteger serialNumber;
    private BigInteger caliber;
    private String name;
}
