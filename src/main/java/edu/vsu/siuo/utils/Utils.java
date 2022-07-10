package edu.vsu.siuo.utils;

import java.util.Random;

public final class Utils {
    public static int rand(int min, int max) {
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        max+=1;
        Random random = new Random();
        if (min < 0) {
            return random.nextInt(max + Math.abs(min)) - Math.abs(min);
        } else {
            return random.nextInt(max - min) + min;
        }
    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
