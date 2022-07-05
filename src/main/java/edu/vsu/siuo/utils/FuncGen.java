package edu.vsu.siuo.utils;

import java.util.Arrays;
import java.util.List;

import static edu.vsu.siuo.utils.Functions.converseToRad;

public final class FuncGen {

    public static List<Integer> genKnpXY(int op_x, int op_y, int d_ok, int a_ok) {
        // перевод из дел.угломера в радианы
        double ugol_rad = converseToRad(a_ok);

        // катеты КНП - ОП
        double kat_knp_x = d_ok * Math.cos(ugol_rad);
        double kat_knp_y = d_ok * Math.cos(ugol_rad);

        // координаты X и Y цели
        int x = (int) Math.round(op_x + kat_knp_x);
        int y = (int) Math.round(op_y + kat_knp_y);

        return Arrays.asList(x, y);
    }
}
