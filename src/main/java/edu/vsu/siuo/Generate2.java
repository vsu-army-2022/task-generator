package edu.vsu.siuo;

import edu.vsu.siuo.domains.AnalysisResult;
import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
import edu.vsu.siuo.domains.dto.ProblemDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.dto.TaskDto;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.vsu.siuo.utils.FuncGen.generateKnp;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;

public class Generate2 {

    private static Map<String, String> GUNS = Map.of(
            "p", "П",
            "u", "У",
            "1", "1",
            "2", "2",
            "3", "3",
            "4", "4");

    private static Map<String, String> TYPES = Map.of(
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

    private static Map<String, String> TARGETS_WORD = Map.of(
            "po", "открыто расположенные ЖС и ОС",
            "pu", "ЖС и ОС, расположенные в окопах (траншеях)",
            "bat", "батарея",
            "vzv", "взвод буксируемых орудий",
            "rap", "радиолокационная станция полевой артиллерии",
            "ptur", "птур в окопе"
    );

    @Data
    private static class Shot_dto {
        int a;
        String type;
        int f;
    }

    public static List<TaskDto> generateTasks(int count) {
        List<TaskDto> taskDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            taskDtos.add(generateTask());
        }
        return taskDtos;
    }

    public static TaskDto generateTask() {
        TaskDto taskDto = new TaskDto();
        ProblemDto problemDto = generateConditionsForTask();

        taskDto.setProblemDto(problemDto);
        taskDto.setSolutionDto(generateSolution(problemDto));
        return taskDto;
    }

    private static OP generateOP (){
        OP op = new OP();

        op.setMainDirection(rand(1, 60) * 100);
        op.setX(rand(20000, 97000));
        op.setY(rand(20000, 97000));
        op.setH(rand(40, 180));

        return op;
    }

    private static ObjectPosition generateKNPfromOP(OP op){
        int distanceFromOPtoKNP = rand(2500, 5200); // расстояние между ОП и КНП
        int angleFromONtoKNP = Math.abs(op.getMainDirection() + (rand(0,1) == 1 ? -1: 1) * rand(50, 1450));
        if (angleFromONtoKNP > 6000) angleFromONtoKNP = angleFromONtoKNP - 6000;

        return generateKnp(op.getX(), op.getY(), distanceFromOPtoKNP, angleFromONtoKNP);
    }
    
    private static Target generateTarget(OP op){
        int angularMagnitude_target = (rand(0, 1) == 1 ? 1 : -1) * rand(1, 20); //угловая величина от низа цели, до ее верха (при наблюдении с кнп)

        // генерируем характер цели;
        Targets targetType = Targets.values()[rand(0, Targets.values().length)];

        int angleFromKNPtoTarget = Math.abs(op.getMainDirection() + (rand(0,1) == 1 ? -1: 1) * rand(50, 1450));
        if (angleFromKNPtoTarget > 6000) angleFromKNPtoTarget = angleFromKNPtoTarget - 6000;

        int targetsDepth = 0; //глубина цели
        int targetsFrontDu = 0; //фронт цели
        int distanceFromKNPtoTarget = rand(2100, 4200); // от кнп до цели
        if (targetType.equals(Targets.PO) || targetType.equals(Targets.PU)) {
            targetsDepth = rand(30, 200);
            targetsFrontDu = rand(Math.round(150 * 1000 / distanceFromKNPtoTarget), Math.round(300 * 1000 / distanceFromKNPtoTarget)); // 150 - 300 м ширина
        }
        if (targetType.equals(Targets.BATR)) {
            targetsDepth = rand(40, 120);
            targetsFrontDu = rand(Math.round(180 * 1000 / distanceFromKNPtoTarget), Math.round(240 * 1000 / distanceFromKNPtoTarget));  // 180 - 240 м ширина
        }
        if (targetType.equals(Targets.VZV)) {
            targetsDepth = rand(40, 90);
            targetsFrontDu = rand(Math.round(90 * 1000 / distanceFromKNPtoTarget), Math.round(120 * 1000 / distanceFromKNPtoTarget));  // 90 - 120 м ширина
        }
        
        Target target = new Target();
        target.setTargetsDepth(targetsDepth);
        target.setType(targetType);
        target.setTargetsFrontDu(targetsFrontDu);
        target.setAngleFromKNPtoTarget(angleFromKNPtoTarget);
        target.setDistanceFromKNPtoTarget(distanceFromKNPtoTarget);
        
        return target;
    }


    public static ProblemDto generateConditionsForTask() {
        // todo validation

        List<TaskDto> taskDtos = new ArrayList<>();

        int rashod = 0;

        OP op = generateOP();
        ObjectPosition knp = generateKNPfromOP(op);
        Target target = generateTarget(op);
        
        // fixme np_x, np_y = null?
        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        // fixme try generate again
        if (!(analysisResult.getPs() < 490) || !(analysisResult.getDovTop() < 380) || !(analysisResult.getDovTop() > -380) || Math.abs(target.getAngleFromKNPtoTarget() - op.getMainDirection()) >= 750) {
            return new ProblemDto();
        }

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
            ku = round(distanceFromKNPtoTarget / analysisResult.getDalTop(), 1);
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
            muD = distanceFromKNPtoTarget / 1000 * sin_pc;
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

        if (targetsDepth < 100) {
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

        if (targetsFrontDu == 0) {
            shot.get(3).setF(rand(Math.round(14 * 1000 / distanceFromKNPtoTarget), Math.round(28 * 1000 / distanceFromKNPtoTarget))); // rand(round(40*1000/distanceFromKNPtoTarget),round(280*1000/distanceFromKNPtoTarget));
            shot.get(4).setF(rand(Math.round(14 * 1000 / distanceFromKNPtoTarget), Math.round(28 * 1000 / distanceFromKNPtoTarget)));
        } else {
            if (targetsFrontDu < 120) {
                shot.get(3).setF(targetsFrontDu + rand(Math.round(90 * 1000 / distanceFromKNPtoTarget), Math.round(120 * 1000 / distanceFromKNPtoTarget)));
            } else {
                shot.get(3).setF(targetsFrontDu + rand(Math.round(140 * 1000 / distanceFromKNPtoTarget), Math.round(190 * 1000 / distanceFromKNPtoTarget)));
            }
            shot.get(4).setF(targetsFrontDu + (rand(0, 1) == 1 ? 1 : -1) * rand(Math.round(6 * 1000 / distanceFromKNPtoTarget), Math.round(28 * 1000 / distanceFromKNPtoTarget)));
        }

        if (targetsDepth < 100) {
            shot.get(4).setType(gen_n.get(rand(5, 6)));
        } else {
            shot.get(4).setType(gen_n.get(rand(7, 8)));
        }
        shot.get(4).setA((rand(0, 1) == 1 ? 1 : -1) * rand(2, 16));

        // уровень
        long urov = 0;
        if (analysisResult.getDalTop() != 0) {
            int target_h = knp.getH() + distanceFromKNPtoTarget * angularMagnitude_target/ 1000; //высота цели
            double angularMagnitude_op = (target_h - op.getH()) / analysisResult.getDalTop() * 1000;

            urov = 3000 + Math.round(angularMagnitude_op);
        }

        List<Double> ts_result = ts(zaryd, dal_isch);
        double pric = ts_result.get(0);
        double dxt = ts_result.get(1);
        double vd = ts_result.get(2);
        pric = Math.round(pric);

        String vzr = null;
        if (targetType.equals("po") || targetType.equals("rap") || targetType.equals("ptur")) {

            vzr = "О";
        }
        if (targetType.equals("pu") || targetType.equals("bat") || targetType.equals("vzv")) {
            vzr = "О и Ф";
        }

        // result
        ProblemDto problemDto = new ProblemDto(op, knp);

        problemDto.setLoad(zaryd);

        List<Integer> distance = List.of(grp.get(0).get("D") / 1000, grp.get(1).get("D") / 1000, grp.get(2).get("D") / 1000);
        problemDto.setDistance(distance);

        List<Integer> range = List.of(grp.get(0).get("dD"), grp.get(1).get("dD"), grp.get(2).get("dD"));
        problemDto.setRange(range);

        List<Integer> direction = List.of(grp.get(0).get("dd"), grp.get(1).get("dd"), grp.get(2).get("dd"));
        problemDto.setDirection(direction);

        problemDto.setTargetType(targetType);
        problemDto.setAlphaC(angleFromKNPtoTarget);
        problemDto.setDK(distanceFromKNPtoTarget);
        problemDto.setEpsC(angularMagnitude_target);
        problemDto.setFDu(targetsFrontDu);
        problemDto.setGC(targetsDepth);

        long gc_op = 0;
        long fcdu_op = 0;
        if (analysisResult.getPs() > 500) {
            double pr1 = Math.abs(targetsFrontDu * muD);
            double pr2 = Math.abs(targetsDepth * kc);

            double ugl1 = Math.abs(targetsFrontDu * ku * kc);
            double ugl2 = Math.abs(targetsDepth / 100 * shu100);

            gc_op = Math.round(pr1 + pr2);
            fcdu_op = Math.round(ugl1 + ugl2);

            // todo check it
            targetsDepth = (int) gc_op;
            targetsFrontDu = (int) fcdu_op;
        }

        double bat_veer;
        String bat_veer_v;
        if (targetsFrontDu == 0) {
            bat_veer_v = "сосредоточенный";
        } else {
            if (analysisResult.getPs() < 500) {

                bat_veer = targetsFrontDu / 6 * ku;
            } else {
                bat_veer = targetsFrontDu / 6;
            }
            bat_veer_v = modAngDash(bat_veer);
        }

        // todo fixme
        int fcm = (targetsFrontDu * distanceFromKNPtoTarget) / 1000;


        if (targetsDepth > 200) targetsDepth = 200;

        if (dxt == 0) dxt = 0.0001;


        int uu = 0;
        // скачок, УУ, УП
        if (fcm > 300 && targetType.equals("po") || fcm > 150 && targetType.equals("pu")) {
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
        if (targetsDepth >= 100) {
            up = 3;
            skachok = " скачок " + Math.round(targetsDepth / 3 / dxt) + ',';
        } else {
            up = 1;
            skachok = "";
        }

        return problemDto;
    }

    public static SolutionDto generateSolution(ProblemDto problemDto) {
        SolutionDto solutionDto = new SolutionDto();

        int mainDirection = problemDto.getOp().getMainDirection();
        int op_x = problemDto.getOp().getX();
        int op_y = problemDto.getOp().getY();
        int hop = problemDto.getOp().getH();
        int knp_x = problemDto.getKnp().getX();
        int knp_y = problemDto.getKnp().getY();
        int hknp = problemDto.getKnp().getH();

        // todo change to enum
        Powers load = problemDto.getLoad();
        List<Integer> distance = problemDto.getDistance();
        List<Integer> range = problemDto.getRange();
        List<Integer> direction = problemDto.getRange();

        Targets targetType = problemDto.getTargetType();
        int alphac = problemDto.getAlphaC();
        int distanceFromKNPtoTarget = problemDto.getDK();
        int epsc = problemDto.getEpsC();
        int fdu = problemDto.getFDu();
        long targetsDepth = problemDto.getGC();

        AnalysisResult analysisResult = analyzePuo(distanceFromKNPtoTarget, alphac, knp_x, knp_y, null, null, op_x, op_y, mainDirection);

        solutionDto.setDCt((int) analysisResult.getDalTop());
//        solutionDto.setDeltaDCt((int) ddi);
//        solutionDto.setDCi((int) dal_isch);

        solutionDto.setDeCt((int) analysisResult.getDovTop());
//        solutionDto.setDeltaDeCt((int) dai);
//        solutionDto.setDeCi((int) dov_isch);

//        solutionDto.setKY((int) ku);
//        solutionDto.setShY((int) shu);
//        solutionDto.setDeltaX((int) dxt);

        solutionDto.setPs((int) analysisResult.getPs());

        solutionDto.setOp(analysisResult.getOpDir());
//        solutionDto.setVD((int) vd);

        if (analysisResult.getPs() > 500) {
//            solutionDto.setFDuOp((int) fcdu_op);
//            solutionDto.setGCOp((int) gc_op);
        }

        List<SolutionDto.TaskCommand> commands = new ArrayList<>();

        SolutionDto.TaskCommand firstCommand = new SolutionDto.TaskCommand();
//        firstCommand.setDescription("«Дон», стой! Цель 21, «" + TARGETS.get(targetType) + "». ОФ, Взрыватель «" + vzr + "». Заряд " + GUNS.get(zaryd) + ". Шкала тысячных, основному 1 сн. Огонь!");
//        firstCommand.setPR((int) pric);
//        firstCommand.setYR((int) urov);
//        firstCommand.setDe("ОН\t" + angDash(dov_isch));
//        firstCommand.setObservation(formatNabl(shot.get(0).getA(), shot.get(0).getType(), shot.get(0).getF(), TYPES));
//        commands.add(firstCommand);
//
//        int flag_k = 1; // для 1 команды батарее
//        int vilka = 200 /*8*vd*/;
//
//        int kol_nabl = 0;
//
//        for (int i = 0; i < 7; i++) {
//            if (shot.get(i) == null) shot.put(i, new Shot_dto());
//            if (shot.get(i).getType() != null && (shot.get(i).getType() != "-11")) {
//                kol_nabl = kol_nabl + 1;
//            }
//        }
//
//        for (int i = 0; i < kol_nabl; i++) {
//
//            int j = i + 2; // счетчик выстрелов
//
//            // считываем наблюдения
//            int alfa = shot.get(i).getA();
//            String har = shot.get(i).getType();
//            String har_next = shot.get(i + 1).getType();
//            int fr = shot.get(i).getF();
//
//            String komand = "Огонь!";
//
//            // последний 4 символ в строке
//            String per_ned = String.valueOf(har.charAt(4));
//
//            int dD = 0;
//
//            // поражение цели
//            if (!har.equals("one_n") && !har.equals("one_p") && !har.equals("-11") && !har.equals("xz")) {
//
//
//                if (targetsDepth < 100) {
//                    if (har.equals("all_n") || har.equals("all_p")) dD = 50;
//                    if (har.equals("pre_n") || har.equals("pre_p")) dD = 25;
//                }
//                if (targetsDepth >= 100) {
//                    if (har.equals("all_n") || har.equals("all_p")) dD = targetsDepth;
//                    if (har.equals("pre_n") || har.equals("pre_p")) dD = Math.round(2 / 3 * targetsDepth);
//                    if (har.equals("rav_n") || har.equals("rav_p")) dD = Math.round(1 / 2 * targetsDepth);
//                }
//
//                int koef_fr;
//
//                if (targetsFrontDu != 0) {
//                    koef_fr = fr / targetsFrontDu;
//                } else {
//                    koef_fr = 0;
//                }
//
//                // todo fr != null
//                if (koef_fr >= 1.5 && analysisResult.getPs() <= 500) { // если есть фронт разрыва
//                    int veer_raz = (int) ((targetsFrontDu - fr) / 6 * ku);
//                    if (veer_raz < -0.99)
//                        komand = "Соединить к основному в " + modAngDash(veer_raz) + ". Огонь!";
//                    else if (veer_raz > 0.99)
//                        komand = "Разделить от основного в " + modAngDash(veer_raz) + ". Огонь!";
//                }
//
//                rashod += 6 * uu * up * 2;
//            } else {
//                dD = vilka;
//
//                if (har.equals("xz")) { // вилка
//                    dD = 0;
//                } else if (!har.equals(har_next) || vilka < 200 /*8*vd*/) {
//                    vilka = vilka / 2;
//                }
//
//                rashod++;
//            }
//
//
//            // 1 команда на поражение батарее
//            if (har_next != null && !har_next.equals("one_n") && !har_next.equals("one_p") && !har_next.equals("net") && !har_next.equals("xz")) {
//                if (flag_k == 1) {
//                    komand = "Батарее! Веер " + bat_veer_v + ',' + uu_v + skachok + " по 2 снаряда беглый. Огонь!";
//                    flag_k = 0;
//                }
//            }
//
//            int betta = 0;
//            int pricel;
//            if (analysisResult.getPs() <= 500) { // по формулам
//
//
//                pricel = (int) Math.round(dD / dxt);
//                if (per_ned.equals("p")) pricel *= -1;
//
//                int kof_1 = (int) (-alfa * ku);
//                int kof_2 = (int) (dD / 100 * shu);
//                if ((analysisResult.getOpDir().equals("l") && per_ned.equals("n")) || (analysisResult.getOpDir().equals("r") && per_ned.equals("p"))) {
//                    kof_2 = -1 * kof_2;
//                }
//                betta = Math.round(kof_1 + kof_2);
//            } else { // по ПРК
//
//                if (per_ned.equals("n")) dD *= -1;
//
//                int pr1 = (int) (alfa * muD);
//                if (analysisResult.getOpDir().equals("l")) pr1 *= -1;
//                int pr2 = (int) (-dD * kc);
//
//                int ugl1 = (int) (-alfa * ku * kc);
//                int ugl2 = (int) (dD / 100 * shu100);
//                if (analysisResult.getOpDir().equals("r")) ugl2 *= -1;
//
//                pricel = (int) Math.round(pr1 / dxt + pr2 / dxt);
//                betta = Math.round(ugl1 + ugl2);
//
//            }
//
//
//            // формируем наблюдение
//            String nablud = (i + 1 == kol_nabl ? "Цель подавлена" : formatNabl(shot.get(i + 1).getA(), shot.get(i + 1).getType(), shot.get(i + 1).getF(), TYPES));
//
//            TaskCommand command = new TaskCommand();
//            command.setDescription(komand);
//            command.setPR(pricel);
//            command.setDe(String.valueOf(betta));
//            command.setObservation(nablud);
//            commands.add(command);
//        }
//
//        TaskCommand lastCommand = new TaskCommand();
//        lastCommand.setDescription("Стой, записать! Цель 21, «" + TARGETS.get(targetType) + "». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход " + rashod + ". Я «Амур».");
//        commands.add(lastCommand);
//
//        solutionDto.setCommands(commands);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setProblemDto(problemDto);
//        taskDto.setSolutionDto(solutionDto);
//
//        taskDtos.add(taskDto);

        return solutionDto;
    }
}