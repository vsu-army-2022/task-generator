package edu.vsu.siuo.utils;

import java.util.Random;

public final class Utils {
    public static int rand(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
