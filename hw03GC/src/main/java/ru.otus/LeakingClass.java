package ru.otus;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LeakingClass {
    private static final BigInteger FORTY_FIVE_CALIBER = BigInteger.valueOf(45);
    private static final int MAGAZINE_SIZE = 40000;

    public static void main(String... args) {
        List<Bullet> bulletsThatHitTheTarget = new ArrayList();
        List<Bullet> bulletsShotInRound = new ArrayList();
        var serial = BigInteger.ZERO;
        var numberOfBulletsFired = BigInteger.ZERO;
        while (true) {
            for (int i = 0; i < MAGAZINE_SIZE; i++) {
                bulletsShotInRound.add(new Bullet(serial, FORTY_FIVE_CALIBER, "For VM with love " + serial));
                serial.add(BigInteger.ONE);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
            for (int i = 0; i < (MAGAZINE_SIZE / 2); i++) {
                bulletsThatHitTheTarget.add(bulletsShotInRound.get(i));
            }
            bulletsShotInRound = new ArrayList<>();
            numberOfBulletsFired = numberOfBulletsFired.add(BigInteger.valueOf(MAGAZINE_SIZE));
            System.out.println(String.format("Another 40000 bullets fired! Total shots: %s", numberOfBulletsFired.toString()));

        }
    }
}
