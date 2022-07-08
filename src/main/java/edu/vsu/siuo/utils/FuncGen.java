package edu.vsu.siuo.utils;

import java.util.Arrays;
import java.util.List;

import static edu.vsu.siuo.utils.Functions.converseToRad;

public final class FuncGen {

    public static List<Integer> genKnpXY(int op_x, int op_y, int distanceFromOPtoKNP, int angleFromONtoKNP) {
        // перевод из дел.угломера в радианы
        double ugol_rad = converseToRad(angleFromONtoKNP);

        // катеты КНП - ОП
        double kat_knp_x = distanceFromOPtoKNP * Math.cos(ugol_rad);
        double kat_knp_y = distanceFromOPtoKNP * Math.cos(ugol_rad);

        // координаты X и Y цели
        int x = (int) Math.round(op_x + kat_knp_x);
        int y = (int) Math.round(op_y + kat_knp_y);

        return Arrays.asList(x, y);
    }
}
