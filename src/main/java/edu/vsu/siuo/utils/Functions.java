package edu.vsu.siuo.utils;

import java.math.BigInteger;

public class Functions {

    public double converseToRad(double a) {
        return a * Math.PI / 30 / 100;
    }

    public double converseToDelAngle(double rad) {
        return Math.round(rad * 30 / Math.PI * 100) % 6000;
    }

    public String angDash(double a) {
        double b = Math.abs(a);
        double c = Math.floor(b / 100);
        double d = b % 100;
        String dd = Double.toString(d);
        if (d < 10) {
            dd = "0" + d;
        }

        String kkk = (a < 0 ? "-" : "+") + c + "-" + dd;

        if (kkk.equals("+0-00")) {
            kkk = "";
        }

        return kkk;
    }

    public String grpCount()
}
