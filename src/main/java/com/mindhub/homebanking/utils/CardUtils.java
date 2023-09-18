package com.mindhub.homebanking.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class CardUtils {
    @NotNull
    public static String getCardNumber() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000)) + "-" +
                String.format("%04d", random.nextInt(10000)) + "-" +
                String.format("%04d", random.nextInt(10000)) + "-" +
                String.format("%04d", random.nextInt(10000));
    }

    public static String getCardCvv() {
        Random random = new Random();
        return String.format("%03d",random.nextInt(1000));
    }
}
