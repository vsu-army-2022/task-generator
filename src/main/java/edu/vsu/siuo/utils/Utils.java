package edu.vsu.siuo.utils;

import java.util.Random;

public final class Utils {
    public static int rand(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
