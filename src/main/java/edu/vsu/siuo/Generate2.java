package edu.vsu.siuo;

import edu.vsu.siuo.domains.AnalysisResult;
import edu.vsu.siuo.domains.TaskDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.vsu.siuo.utils.FuncGen.genKnpXY;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;

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

            int a_ok = 0;
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

            List<Integer> knpXY = genKnpXY(op_x, op_y, d_ok, a_ok);
            int knp_x = knpXY.get(0);
            int knp_y = knpXY.get(1);

            int knp_h = rand(40, 180);
            int ec_knp = (rand(0, 1) == 1 ? 1 : -1) * rand(1, 20);

            // генерируем характер цели
            List<String> gen_h = List.of("po", "pu", "vzv", "bat", "ptur", "rap");
            String c_type = gen_h.get(rand(0, 5));

            int gc = 0;
            int fcdu = 0;
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

            if (analysisResult.getPs() < 490 && analysisResult.getDovTop() < 380 && analysisResult.getDovTop() > -380 && Math.abs(ak - on) < 750) {

                zad_ps5++;

                // генерируем ГРП
                Map<Integer, Map<String, Integer>> grp = new HashMap<>();
                grp.put(0, new HashMap<>());
                grp.put(1, new HashMap<>());
                grp.put(2, new HashMap<>());


                //todo check it
                // берем первый разряд dal_top и прибавляем rand(0, 1)
                grp.get(1).put("D", ((int) analysisResult.getDovTop()) / 1000 + rand(0, 1));
                grp.get(0).put("D", grp.get(1).get("D") - 2);
                grp.get(2).put("D", grp.get(1).get("D") + 2);

                for (int i = 0; i < 3; i++) {
                    // todo check it
                    grp.get(i).put("D", grp.get(i).get("D") * 1000);
                }

                if (rand(0, 1) == 1) { // дальность
                    grp.get(0).put("dD", rand(43, 260));
                    grp.get(1).put("dD", grp.get(0).get("dD") + rand(90, 180));
                    grp.get(2).put("dD", grp.get(1).get("dD") + rand(90, 180));
                } else {
                    grp.get(0).put("dD", rand(-43, -260));
                    grp.get(1).put("dD", grp.get(0).get("dD") + rand(-60, -150));
                    grp.get(2).put("dD", grp.get(1).get("dD") + rand(-60, -150));
                }

                if (rand(0, 1) == 1) {  // направление
                    grp.get(0).put("dd", rand(3, 15));
                    grp.get(1).put("dd", grp.get(0).get("dd") + rand(2, 8));
                    grp.get(2).put("dd", grp.get(1).get("dd") + rand(2, 8));
                } else {
                    grp.get(0).put("dd", rand(-3, -15));
                    grp.get(1).put("dd", grp.get(0).get("dd") + rand(-2, -8));
                    grp.get(2).put("dd", grp.get(1).get("dd") + rand(-2, -8));
                }

                double ku, shu;

                if (analysisResult.getDalTop() != 0) {
                    ku = round(dk / analysisResult.getDalTop(), 1);
                    shu = Math.round(analysisResult.getPs() / analysisResult.getDalTop() * 100);
                }

                double ps_rad, sin_pc, kc, muD, shu100;

                if (analysisResult.getPs() > 500) { // ПРК: находим коэффециенты Мюд,Кс, Шу100
                    ps_rad = converseToRad(analysisResult.getPs());
                    sin_pc = round(Math.sin(ps_rad), 2);
                    kc = round(Math.cos(ps_rad), 2);
                    muD = dk / 1000 * sin_pc;
                    shu100 = sin_pc * 100000 / analysisResult.getDalTop();
                    //echo 'kc = '.kc.' muD = '.muD.' shu100 = '.shu100;
                }

                // исчисленная дальность и доворот
                Map<String, Double> grp_count = grpCount(grp, analysisResult.getDalTop());
                double ddi = grp_count.get(0);
                double dai = grp_count.get(1);


                double dal_isch = analysisResult.getDalTop() + ddi;
                double dov_isch = analysisResult.getDovTop() + dai;


                // генерируем заряд
                List<String> gen_z = List.of("p", "u", "1", "2", "3", "4");

                String zaryd = null;
                if (dal_isch < 4800) {
                    zaryd = gen_z.get(rand(4, 5));
                } else if (dal_isch >= 4800 && dal_isch < 7400) {
                    zaryd = gen_z.get(rand(2, 3));
                } else if (dal_isch >= 7400) {
                    zaryd = gen_z.get(rand(0, 1));
                }


                List<String> gen_n = List.of("xz", "one_p", "one_n", "all_p", "all_n", "pre_p", "pre_n", "rav_p", "rav_n");
                // генерируем наблюдения

                Map<Integer, Map<String, Object>> shot = new HashMap<>();

                if (gc < 100) {
                    shot.put(0, new HashMap<>());
                    shot.get(0).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
                    shot.get(0).put("type", gen_n.get(rand(1, 2)));

                    shot.put(1, new HashMap<>());
                    shot.get(1).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
                    shot.get(1).put("type", shot.get(0).get("type") == "one_n" ? "one_p" : "one_n");

                    shot.put(2, new HashMap<>());
                    shot.get(2).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));
                    shot.get(2).put("type", gen_n.get(rand(1, 2)));
                } else {
                    shot.get(0).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
                    shot.get(0).put("type", gen_n.get(rand(0, 2)));

                    shot.get(1).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
                    shot.get(2).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));

                    if (shot.get(0).get("type") == "xz") {
                        shot.get(1).put("type", gen_n.get(rand(1, 2)));
                        shot.get(2).put("type", (shot.get(1).get("type") == "one_n" ? "one_p" : "one_n"));
                    } else {
                        shot.get(1).put("type", shot.get(0).get("type") == "one_p" ? "one_p" : "one_n");
                        shot.get(2).put("type", (shot.get(1).get("type") == "one_n" ? "one_p" : "one_n"));
                    }
                }

                shot.get(3).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(5, 21));
                shot.get(3).put("type", gen_n.get(rand(3, 4)));

                if (fcdu == 0) {
                    shot.get(3).put("f", rand(Math.round(14 * 1000 / dk), Math.round(28 * 1000 / dk))); // rand(round(40*1000/dk),round(280*1000/dk));
                    shot.get(4).put("f", rand(Math.round(14 * 1000 / dk), Math.round(28 * 1000 / dk)));
                } else {
                    if (fcdu < 120) {
                        shot.get(3).put("f", fcdu + rand(Math.round(90 * 1000 / dk), Math.round(120 * 1000 / dk)));
                    } else {
                        shot.get(3).put("f", fcdu + rand(Math.round(140 * 1000 / dk), Math.round(190 * 1000 / dk)));
                    }
                    shot.get(4).put("f", fcdu + (rand(0, 1) == 1 ? 1 : -1) * rand(Math.round(6 * 1000 / dk), Math.round(28 * 1000 / dk)));
                }

                if (gc < 100) {
                    shot.get(4).put("type", gen_n.get(rand(5, 6)));
                } else {
                    shot.get(4).put("type", gen_n.get(rand(7, 8)));
                }
                shot.get(4).put("a", (rand(0, 1) == 1 ? 1 : -1) * rand(2, 16));

                // уровень
                if (analysisResult.getDalTop() != 0) {
                    int c_h = knp_h + ec_knp * dk / 1000;
                    double ec_op = (c_h - op_h) / analysisResult.getDalTop() * 1000;
                    long urov = 3000 + Math.round(ec_op);
                }

                List<Double> ts_result = ts(zaryd, dal_isch);
                double pric = ts_result.get(0);
                double dxt = ts_result.get(1);
                double vd = ts_result.get(2);
                pric = Math.round(pric);

                if (c_type.equals("po") || c_type.equals("rap") || c_type.equals("ptur")) {
                    String vzr = "О";
                }
                if (c_type.equals("pu") || c_type.equals("bat") || c_type.equals("vzv")) {
                    String vzr = "О и Ф";
                }

                // result
                TaskDto taskDto = new TaskDto();

                taskDto.setOH(on);

                taskDto.setXop(op_x);
                taskDto.setYop(op_y);
                taskDto.setHop(op_h);
                taskDto.setXknp(knp_x);
                taskDto.setYknp(knp_y);
                taskDto.setHknp(knp_h);

                taskDto.setLoad(zaryd);
            }
        }
    }
}