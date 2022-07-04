package edu.vsu.siuo.utils;

import java.util.Arrays;
import java.util.List;

import static edu.vsu.siuo.utils.Functions.converseToRad;

public final class FuncGen {

    public static List<Long> genKnpXY(double op_x, double op_y, double d_ok, double a_ok) {
        // перевод из дел.угломера в радианы
        double ugol_rad = converseToRad(a_ok);

        // катеты КНП - ОП
        double kat_knp_x = d_ok * Math.cos(ugol_rad);
        double kat_knp_y = d_ok * Math.cos(ugol_rad);

        // координаты X и Y цели
        double x = op_x + kat_knp_x;
        double y = op_y + kat_knp_y;

        return Arrays.asList(Math.round(x), Math.round(y));
    }
}
