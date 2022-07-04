package edu.vsu.siuo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Functions {

    public static double converseToRad(double a) {
        return a * Math.PI / 30 / 100;
    }

    public static double converseToDelAngle(double rad) {
        return Math.round(rad * 30 / Math.PI * 100) % 6000;
    }

    public static String angDash(double a) {
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

    public static Map<String, Double> grpCount(Map<String, Map<Integer, Double>> d, double strD) {
        IntStream.range(0, 2).forEach(i -> d.get("D").put(i, d.get("D").get(i) - d.get("dD").get(i)));

        Map<String, Double> left = new HashMap<>();
        Map<String, Double> right = new HashMap<>();
        if (strD < d.get("D").get(1)) {
            sidePut(d, left, 0);
            sidePut(d, right, 1);
        } else {
            sidePut(d, left, 1);
            sidePut(d, right, 2);
        }

        Map<String, Double> ret = new HashMap<>();
        d.keySet().stream().filter(grp -> !grp.equals("D")).forEach(grp -> {
            double tmp = left.get(grp) +
                         (right.get(grp) - left.get(grp)) *
                         (strD - left.get("D")) /
                         (right.get("D") - left.get("D"));
            ret.put(grp, tmp);
        });

        ret.replaceAll((g, v) -> (double) Math.round(ret.get(g)));
        return ret;
    }

    private static void sidePut(Map<String, Map<Integer, Double>> d, Map<String, Double> side, int i) {
        d.keySet().forEach(grp -> side.put(grp, d.get(grp).get(i)));
    }
}
