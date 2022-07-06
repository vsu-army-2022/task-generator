package edu.vsu.siuo.utils;

import org.junit.jupiter.api.Test;

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
    public void angDash_1(){
        double a = 1.0;

        String result = Functions.angDash(a);

        assertEquals("+0-01", result);
    }

    @Test
    public void angDash_2(){
        double a = -15;

        String result = Functions.angDash(a);

        assertEquals("-0-15", result);
    }

    @Test
    public void modAngDash_1(){
        double a = -978;

        String result = Functions.modAngDash(a);

        assertEquals("9-78", result);
    }

    @Test
    public void modAngDash_2(){
        double a = 3200;

        String result = Functions.modAngDash(a);

        assertEquals("32-00", result);
    }
}