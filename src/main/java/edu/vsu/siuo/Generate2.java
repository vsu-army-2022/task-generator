package edu.vsu.siuo;

import edu.vsu.siuo.domains.AnalysisResult;

import java.util.List;

import static edu.vsu.siuo.utils.FuncGen.genKnpXY;
import static edu.vsu.siuo.utils.Functions.analyzePuo;
import static edu.vsu.siuo.utils.Utils.rand;

public class Generate2 {

    public void generate(int kol_varik) {
        // todo validation

        int zad_ps5 = 0;
        // общий цикл: множество вариантов
        while (zad_ps5 < kol_varik) {

            int rashod = 0;

            int on = rand(1, 60) * 100;

            int op_x = rand(20000, 97000);
            int op_y = rand(20000, 97000);
            int op_h = rand(40, 180);

            int d_ok = rand(2500, 5200); // расстояние между ОП и КНП
            int dk = rand(2100, 4200); // от кнп до цели

            int a_ok;
            int ak = 0;

            if (on > 5250 && on <= 6000) {
                a_ok = rand(5300, 5950);
                ak = rand(5300, 5950);
            }
            if (on > 0 && on <= 750) {
                a_ok = rand(100, 700);
                ak = rand(100, 700);
            }
            if (on > 750 && on <= 2250) {
                a_ok = rand(800, 2200);
                ak = rand(800, 2200);
            }
            if (on > 2250 && on <= 3750) {
                a_ok = rand(2300, 3700);
                ak = rand(2300, 3700);
            }
            if (on > 3750 && on <= 5250) {
                a_ok = rand(3800, 5200);
                ak = rand(3800, 5200);
            }

            List<Long> knpXY = genKnpXY(op_x, op_y, d_ok, a_ok);
            double knp_x = knpXY.get(0);
            double knp_y = knpXY.get(1);

            int knp_h = rand(40, 180);
            int eс_knp = (rand(0, 1) == l ? 1 : -1) * rand(1, 20);

            // генерируем характер цели
            List<String> gen_h = List.of("po", "pu", "vzv", "bat", "ptur", "rap");
            String c_type = gen_h.get(rand(0, 5));

            int gc;
            int fcdu;
            if (c_type.equals("po") || c_type.equals("pu")) {
                gc = rand(30, 200);
                fcdu = rand(Math.round(150 * 1000 / dk), Math.round(300 * 1000 / dk)); // 300 м (максимальная ширина цели)
            }
            if (c_type.equals("bat")) {
                gc = rand(40, 120);
                fcdu = rand(Math.round(180 * 1000 / dk), Math.round(240 * 1000 / dk));
            }
            if (c_type.equals("vzv")) {
                gc = rand(40, 90);
                fcdu = rand(Math.round(90 * 1000 / dk), Math.round(120 * 1000 / dk));
            }
            if (c_type.equals("rap") || c_type.equals("ptur")) {
                gc = 0;
                fcdu = 0;
            }

            // fixme np_x, np_y = null?
            AnalysisResult analysisResult = analyzePuo(dk, ak, knp_x, knp_y, null, null, op_x, op_y, on);

            //todo ...
        }
    }
}