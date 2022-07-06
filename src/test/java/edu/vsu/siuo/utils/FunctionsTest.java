package edu.vsu.siuo.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionsTest {

    private final double DELTA = 0.01;

    @Test
    public void converseToRad() {
        int angle = 3459;

        double result = Functions.converseToRad(angle);

        assertEquals(3.6222, result, DELTA);
    }

    @Test
    public void converseToDelAngle() {
        double angle = 4.8466924104941;

        int result = Functions.converseToDelAngle(angle);

        assertEquals(4628, result, DELTA);
    }

    @Test
    public void angDash_1() {
        double a = 1.0;

        String result = Functions.angDash(a);

        assertEquals("+0-01", result);
    }

    @Test
    public void angDash_2() {
        double a = -15;

        String result = Functions.angDash(a);

        assertEquals("-0-15", result);
    }

    @Test
    public void modAngDash_1() {
        double a = -978;

        String result = Functions.modAngDash(a);

        assertEquals("9-78", result);
    }

    @Test
    public void modAngDash_2() {
        double a = 3200;

        String result = Functions.modAngDash(a);

        assertEquals("32-00", result);
    }

    @Test
    public void grpCount_1() {
        int d = 4978;
        Map<Integer, Map<String, Integer>> grp = new HashMap<>();
        grp.put(1, Map.of("D", 4247, "dD", -247, "dd", 14));
        grp.put(0, Map.of("D", 2155, "dD", -155, "dd", 10));
        grp.put(2, Map.of("D", 6339, "dD", -339, "dd", 20));

        List<Double> result = Functions.grpCount(grp, d);
        assertEquals(-279, result.get(0));
        assertEquals(-16, result.get(1));
    }

    @Test
    public void grpCount_2() {
        int d = 4612;
        Map<Integer, Map<String, Integer>> grp = new HashMap<>();
        grp.put(1, Map.of("D", 5397, "dD", -397, "dd", 17));
        grp.put(0, Map.of("D", 3255, "dD", -255, "dd", 12));
        grp.put(2, Map.of("D", 7468, "dD", -468, "dd", 23));

        List<Double> result = Functions.grpCount(grp, d);
        assertEquals(-345, result.get(0));
        assertEquals(-15, result.get(1));
    }
}