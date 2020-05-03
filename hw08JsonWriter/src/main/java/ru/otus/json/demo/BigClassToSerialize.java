package ru.otus.json.demo;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BigClassToSerialize {
    private boolean bigBoolean = true;
    private char bigLetter = 'B';
    private byte bigByte = (byte) 256;
    public short bigShort = 512;
    protected int bigInt = 1024;
    long bigLong = -2048;
    float bigFloat = 4096.2f;
    double bigDouble = 8192.111;
    String bigString = "Banana";
    Integer bigInteger = -1024;
    ArrayList<String> bigArrayList = new ArrayList<>();
    ArrayList<Character> bigEmptyArrayList = new ArrayList<>();
    ArrayList<Integer> bigNullArrayList = null;
    BigInteger bigBigInteger = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(Long.MAX_VALUE));
    BigDecimal bigBigDecimal = BigDecimal.valueOf(Double.MAX_VALUE).add(BigDecimal.valueOf(Double.MAX_VALUE));
    Map<Integer, Integer> bigPrimitiveMap;

    @Setter
    Set<SmallClassToSerialize> bigSet;
    @Setter
    Map<Integer, SmallClassToSerialize> bigMap;

    public BigClassToSerialize() {
        bigPrimitiveMap = new HashMap<>();
        bigPrimitiveMap.put(1, 3);
        bigPrimitiveMap.put(3, null);

        bigArrayList.add("BIG");
        bigArrayList.add("IN");
        bigArrayList.add("JAPAN");
    }

    @Override
    public String toString() {
        return "" + bigBoolean + " "
                + bigLetter + " "
                + bigByte + " "
                + bigShort + " "
                + bigInt + " "
                + bigLong + " "
                + bigFloat + " "
                + bigDouble + " "
                + bigString + " "
                + bigInteger + " "
                + bigBigInteger + " "
                + bigArrayList + " "
                + bigEmptyArrayList + " "
                + bigNullArrayList + " "
                + bigBigDecimal + " "
                + bigPrimitiveMap + " "
                + bigSet + " "
                + bigMap + " ";
    }
}
