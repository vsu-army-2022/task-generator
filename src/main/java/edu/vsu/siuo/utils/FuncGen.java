package edu.vsu.siuo.utils;

import edu.vsu.siuo.domains.ObjectPosition;

import java.util.Arrays;
import java.util.List;

import static edu.vsu.siuo.utils.Functions.converseToRad;
import static edu.vsu.siuo.utils.Utils.rand;

public final class FuncGen {

    public static ObjectPosition generateKnp(int op_x, int op_y, int distanceFromOPtoKNP, int angleFromONtoKNP) {
        // перевод из дел.угломера в радианы
        double ugol_rad = converseToRad(angleFromONtoKNP);

        // катеты КНП - ОП
        double kat_knp_x = distanceFromOPtoKNP * Math.cos(ugol_rad);
        double kat_knp_y = distanceFromOPtoKNP * Math.cos(ugol_rad);

        // координаты X и Y цели

        ObjectPosition knp = new ObjectPosition();
        knp.setX((int) Math.round(op_x + kat_knp_x));
        knp.setY((int) Math.round(op_y + kat_knp_y));
        knp.setH(rand(40, 180));

        return knp;
    }
}
