package edu.vsu.siuo;

import edu.vsu.siuo.domains.AnalysisResult;
import edu.vsu.siuo.domains.TaskDto;
import edu.vsu.siuo.domains.enums.Powers;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.vsu.siuo.domains.TaskDto.*;
import static edu.vsu.siuo.utils.FuncGen.genKnpXY;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;

public class Generate2 {
    @Data
    private static class Shot_dto{
        int a;
        String type;
        int f;
    }

    public static List<TaskDto> generate(int taskCount) {
        // todo validation

        Map<String, String> GUNS = Map.of(
                "p", "П",
                "u", "У",
                "1", "1",
                "2", "2",
                "3", "3",
                "4", "4");

        Map<String, String> TYPES = Map.of(
                "-11", "",
                "xz", "«?»",
                "one_p", "«+»",
                "one_n", "«-»",
                "all_p", "Все «+»",
                "all_n", "Все «-»",
                "pre_p", "Преобладание «+»",
                "pre_n", "Преобладание «-»",
                "rav_p", "Равенство «+» и «-» от-но ДГЦ",
                "rav_n", "Равенство «+» и «-» от-но БГЦ"
        );

        Map<String, String> TARGETS_WORD = Map.of(
                "po", "открыто расположенные ЖС и ОС",
                "pu", "ЖС и ОС, расположенные в окопах (траншеях)",
                "bat", "батарея",
                "vzv", "взвод буксируемых орудий",
                "rap", "радиолокационная станция полевой артиллерии",
                "ptur", "птур в окопе"
        );

        Map<String, String> TARGETS = Map.of(
                "po", "пехота",
                "pu", "пехота укрытая",
                "bat", "батарея",
                "vzv", "взвод артиллерийский",
                "rap", "РЛС",
                "ptur", "птур в окопе"
        );

        List<TaskDto> taskDtos = new ArrayList<>();

        int taskNumber = 0;
        // общий цикл: множество вариантов
        while (taskNumber < taskCount) {

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

                taskNumber++;

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

                double ku = 0, shu = 0;

                if (analysisResult.getDalTop() != 0) {
                    ku = round(dk / analysisResult.getDalTop(), 1);
                    shu = Math.round(analysisResult.getPs() / analysisResult.getDalTop() * 100);
                }

                double ps_rad;
                double sin_pc;
                double kc = 0;
                double muD = 0;
                double shu100 = 0;

                if (analysisResult.getPs() > 500) { // ПРК: находим коэффециенты Мюд,Кс, Шу100
                    ps_rad = converseToRad(analysisResult.getPs());
                    sin_pc = round(Math.sin(ps_rad), 2);
                    kc = round(Math.cos(ps_rad), 2);
                    muD = dk / 1000 * sin_pc;
                    shu100 = sin_pc * 100000 / analysisResult.getDalTop();
                    //echo 'kc = '.kc.' muD = '.muD.' shu100 = '.shu100;
                }

                // исчисленная дальность и доворот
                List<Double> grp_count = grpCount(grp, analysisResult.getDalTop());
                double ddi = grp_count.get(0);
                double dai = grp_count.get(1);


                double dal_isch = analysisResult.getDalTop() + ddi;
                double dov_isch = analysisResult.getDovTop() + dai;

                Powers zaryd = null;
                if (dal_isch < 4800) {
                    zaryd = (rand(3, 4) == 3 ? Powers.Power3 : Powers.Power4);
                } else if (dal_isch >= 4800 && dal_isch < 7400) {
                    zaryd = (rand(1, 2) == 1 ? Powers.Power1 : Powers.Power2);
                } else if (dal_isch >= 7400) {
                    zaryd = (rand(0, 1) == 1 ? Powers.Reduced : Powers.Full);
                }


                List<String> gen_n = List.of("xz", "one_p", "one_n", "all_p", "all_n", "pre_p", "pre_n", "rav_p", "rav_n");
                // генерируем наблюдения

                Map<Integer, Shot_dto> shot = new HashMap<>();

                shot.put(0, new Shot_dto());
                shot.put(1, new Shot_dto());
                shot.put(2, new Shot_dto());

                if (gc < 100) {
                    shot.get(0).setA((rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
                    shot.get(0).setType(gen_n.get(rand(1, 2)));

                    shot.get(1).setA((rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
                    shot.get(1).setType(shot.get(0).getType() == "one_n" ? "one_p" : "one_n");

                    shot.get(2).setA((rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));
                    shot.get(2).setType(gen_n.get(rand(1, 2)));
                } else {
                    shot.get(0).setA((rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
                    shot.get(0).setType(gen_n.get(rand(0, 2)));

                    shot.get(1).setA((rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
                    shot.get(2).setA((rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));

                    if (shot.get(0).getType() == "xz") {
                        shot.get(1).setType(gen_n.get(rand(1, 2)));
                        shot.get(2).setType((shot.get(1).getType() == "one_n" ? "one_p" : "one_n"));
                    } else {
                        shot.get(1).setType(shot.get(0).getType() == "one_p" ? "one_p" : "one_n");
                        shot.get(2).setType((shot.get(1).getType() == "one_n" ? "one_p" : "one_n"));
                    }
                }

                shot.put(3, new Shot_dto());
                shot.get(3).setA((rand(0, 1) == 1 ? 1 : -1) * rand(5, 21));
                shot.get(3).setType(gen_n.get(rand(3, 4)));

                shot.put(4, new Shot_dto());

                if (fcdu == 0) {
                    shot.get(3).setF(rand(Math.round(14 * 1000 / dk), Math.round(28 * 1000 / dk))); // rand(round(40*1000/dk),round(280*1000/dk));
                    shot.get(4).setF(rand(Math.round(14 * 1000 / dk), Math.round(28 * 1000 / dk)));
                } else {
                    if (fcdu < 120) {
                        shot.get(3).setF(fcdu + rand(Math.round(90 * 1000 / dk), Math.round(120 * 1000 / dk)));
                    } else {
                        shot.get(3).setF(fcdu + rand(Math.round(140 * 1000 / dk), Math.round(190 * 1000 / dk)));
                    }
                    shot.get(4).setF(fcdu + (rand(0, 1) == 1 ? 1 : -1) * rand(Math.round(6 * 1000 / dk), Math.round(28 * 1000 / dk)));
                }

                if (gc < 100) {
                    shot.get(4).setType(gen_n.get(rand(5, 6)));
                } else {
                    shot.get(4).setType(gen_n.get(rand(7, 8)));
                }
                shot.get(4).setA((rand(0, 1) == 1 ? 1 : -1) * rand(2, 16));

                // уровень
                long urov = 0;
                if (analysisResult.getDalTop() != 0) {
                    int c_h = knp_h + ec_knp * dk / 1000;
                    double ec_op = (c_h - op_h) / analysisResult.getDalTop() * 1000;

                    urov = 3000 + Math.round(ec_op);
                }

                List<Double> ts_result = ts(zaryd, dal_isch);
                double pric = ts_result.get(0);
                double dxt = ts_result.get(1);
                double vd = ts_result.get(2);
                pric = Math.round(pric);

                String vzr = null;
                if (c_type.equals("po") || c_type.equals("rap") || c_type.equals("ptur")) {

                    vzr = "О";
                }
                if (c_type.equals("pu") || c_type.equals("bat") || c_type.equals("vzv")) {
                    vzr = "О и Ф";
                }

                // result
                TaskDto taskDto = new TaskDto();

                taskDto.setOh(on);

                taskDto.setXOp(op_x);
                taskDto.setYOp(op_y);
                taskDto.setHOp(op_h);
                taskDto.setXKnp(knp_x);
                taskDto.setYKnp(knp_y);
                taskDto.setHKnp(knp_h);

                taskDto.setLoad(zaryd);

                List<Integer> distance = List.of(
                        grp.get(0).get("D") / 1000,
                        grp.get(1).get("D") / 1000,
                        grp.get(2).get("D") / 1000
                );
                taskDto.setDistance(distance);

                List<Integer> range = List.of(
                        grp.get(0).get("dD"),
                        grp.get(1).get("dD"),
                        grp.get(2).get("dD")
                );
                taskDto.setRange(range);

                List<Integer> direction = List.of(
                        grp.get(0).get("dd"),
                        grp.get(1).get("dd"),
                        grp.get(2).get("dd")
                );
                taskDto.setDirection(direction);

                taskDto.setTargetType(c_type);
                taskDto.setAlphaC(ak);
                taskDto.setDK(dk);
                taskDto.setEpsC(ec_knp);
                taskDto.setFDu(fcdu);
                taskDto.setGC(gc);

                long gc_op = 0;
                long fcdu_op = 0;
                if (analysisResult.getPs() > 500) {
                    double pr1 = Math.abs(fcdu * muD);
                    double pr2 = Math.abs(gc * kc);

                    double ugl1 = Math.abs(fcdu * ku * kc);
                    double ugl2 = Math.abs(gc / 100 * shu100);

                    gc_op = Math.round(pr1 + pr2);
                    fcdu_op = Math.round(ugl1 + ugl2);

                    // todo check it
                    gc = (int) gc_op;
                    fcdu = (int) fcdu_op;
                }

                double bat_veer;
                String bat_veer_v;
                if (fcdu == 0) {
                    bat_veer_v = "сосредоточенный";
                } else {
                    if (analysisResult.getPs() < 500) {

                        bat_veer = fcdu / 6 * ku;
                    } else {
                        bat_veer = fcdu / 6;
                    }
                    bat_veer_v = modAngDash(bat_veer);
                }

                // todo fixme
                int fcm = (fcdu * dk) / 1000;


                if (gc > 200) gc = 200;

                if (dxt == 0) dxt = 0.0001;


                int uu = 0;
                // скачок, УУ, УП
                if (fcm > 300 && c_type.equals("po") || fcm > 150 && c_type.equals("pu")) {
                    uu = 2;
                } else {
                    uu = 1;
                }

                String uu_v = "";

                if (uu == 2) {
                    uu_v = " установок " + uu + ',';
                }

                String skachok = "";
                int up = 0;
                if (gc >= 100) {
                    up = 3;
                    skachok = " скачок " + Math.round(gc / 3 / dxt) + ',';
                } else {
                    up = 1;
                    skachok = "";
                }

                taskDto.setDCt((int) analysisResult.getDalTop());
                taskDto.setDCt((int) ddi);
                taskDto.setDCi((int) dal_isch);

                taskDto.setDeCt((int) analysisResult.getDovTop());
                taskDto.setDeltaDeCt((int) dai);
                taskDto.setDeCi((int) dov_isch);

                taskDto.setKY((int) ku);
                taskDto.setShY((int) shu);
                taskDto.setDeltaX((int) dxt);

                taskDto.setPs((int) analysisResult.getPs());

                taskDto.setOp(analysisResult.getOpDir());
                taskDto.setVD((int) vd);

                if (analysisResult.getPs() > 500) {
                    taskDto.setFDuOp((int) fcdu_op);
                    taskDto.setGCOp((int) gc_op);
                }

                List<TaskCommand> commands = new ArrayList<>();

                TaskCommand firstCommand = new TaskCommand();
                firstCommand.setDescription("«Дон», стой! Цель 21, «" + TARGETS.get(c_type) + "». ОФ, Взрыватель «" + vzr + "». Заряд " + GUNS.get(zaryd) + ". Шкала тысячных, основному 1 сн. Огонь!");
                firstCommand.setPR((int) pric);
                firstCommand.setYR((int) urov);
                firstCommand.setDe("ОН\t" + angDash(dov_isch));
                firstCommand.setObservation(formatNabl(shot.get(0).getA(), shot.get(0).getType(), shot.get(0).getF(), TYPES));
                commands.add(firstCommand);

                int flag_k = 1; // для 1 команды батарее
                int vilka = 200 /*8*vd*/;

                int kol_nabl = 0;

                for (int i = 0; i < 7; i++) {
                    if (shot.get(i) == null) shot.put(i, new Shot_dto());
                    if (shot.get(i).getType() != null  && (shot.get(i).getType() != "-11")) {
                        kol_nabl = kol_nabl + 1;
                    }
                }

                for (int i = 0; i < kol_nabl; i++) {

                    int j = i + 2; // счетчик выстрелов

                    // считываем наблюдения
                    int alfa = shot.get(i).getA();
                    String har = shot.get(i).getType();
                    String har_next = shot.get(i + 1).getType();
                    int fr = shot.get(i).getF();

                    String komand = "Огонь!";

                    // последний 4 символ в строке
                    String per_ned = String.valueOf(har.charAt(4));

                    int dD = 0;

                    // поражение цели
                    if (!har.equals("one_n") && !har.equals("one_p") && !har.equals("-11") && !har.equals("xz")) {


                        if (gc < 100) {
                            if (har.equals("all_n") || har.equals("all_p"))
                                dD = 50;
                            if (har.equals("pre_n") || har.equals("pre_p"))
                                dD = 25;
                        }
                        if (gc >= 100) {
                            if (har.equals("all_n") || har.equals("all_p"))
                                dD = gc;
                            if (har.equals("pre_n") || har.equals("pre_p"))
                                dD = Math.round(2 / 3 * gc);
                            if (har.equals("rav_n") || har.equals("rav_p"))
                                dD = Math.round(1 / 2 * gc);
                        }

                        int koef_fr;

                        if (fcdu != 0) {
                            koef_fr = fr / fcdu;
                        } else {
                            koef_fr = 0;
                        }

                        // todo fr != null
                        if (koef_fr >= 1.5 && analysisResult.getPs() <= 500) { // если есть фронт разрыва
                            int veer_raz = (int) ((fcdu - fr) / 6 * ku);
                            if (veer_raz < -0.99)
                                komand = "Соединить к основному в " + modAngDash(veer_raz) + ". Огонь!";
                            else if (veer_raz > 0.99)
                                komand = "Разделить от основного в " + modAngDash(veer_raz) + ". Огонь!";
                        }

                        rashod += 6 * uu * up * 2;
                    } else {
                        dD = vilka;

                        if (har.equals("xz")) { // вилка
                            dD = 0;
                        } else if (!har.equals(har_next) || vilka < 200 /*8*vd*/) {
                            vilka = vilka / 2;
                        }

                        rashod++;
                    }


                    // 1 команда на поражение батарее
                    if (har_next != null && !har_next.equals("one_n") && !har_next.equals("one_p") && !har_next.equals("net") && !har_next.equals("xz")) {
                        if (flag_k == 1) {
                            komand = "Батарее! Веер " + bat_veer_v + ',' + uu_v + skachok + " по 2 снаряда беглый. Огонь!";
                            flag_k = 0;
                        }
                    }

                    int betta = 0;
                    int pricel;
                    if (analysisResult.getPs() <= 500) { // по формулам


                        pricel = (int) Math.round(dD / dxt);
                        if (per_ned.equals("p"))
                            pricel *= -1;

                        int kof_1 = (int) (-alfa * ku);
                        int kof_2 = (int) (dD / 100 * shu);
                        if ((analysisResult.getOpDir().equals("l") && per_ned.equals("n")) || (analysisResult.getOpDir().equals("r") && per_ned.equals("p"))) {
                            kof_2 = -1 * kof_2;
                        }
                        betta = Math.round(kof_1 + kof_2);
                    } else { // по ПРК

                        if (per_ned.equals("n")) dD *= -1;

                        int pr1 = (int) (alfa * muD);
                        if (analysisResult.getOpDir().equals("l")) pr1 *= -1;
                        int pr2 = (int) (-dD * kc);

                        int ugl1 = (int) (-alfa * ku * kc);
                        int ugl2 = (int) (dD / 100 * shu100);
                        if (analysisResult.getOpDir().equals("r")) ugl2 *= -1;

                        pricel = (int) Math.round(pr1 / dxt + pr2 / dxt);
                        betta = Math.round(ugl1 + ugl2);

                    }


                    // формируем наблюдение
                    String nablud = (i + 1 == kol_nabl ? "Цель подавлена" : formatNabl(shot.get(i + 1).getA(), shot.get(i + 1).getType(), shot.get(i + 1).getF(), TYPES));

                    TaskCommand command = new TaskCommand();
                    command.setDescription(komand);
                    command.setPR(pricel);
                    command.setDe(String.valueOf(betta));
                    command.setObservation(nablud);
                    commands.add(command);
                }

                TaskCommand lastCommand = new TaskCommand();
                lastCommand.setDescription("Стой, записать! Цель 21, «" + TARGETS.get(c_type) + "». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход " + rashod + ". Я «Амур».");
                commands.add(lastCommand);

                taskDto.setCommands(commands);
                taskDtos.add(taskDto);
            }

        }
        return taskDtos;
    }
}