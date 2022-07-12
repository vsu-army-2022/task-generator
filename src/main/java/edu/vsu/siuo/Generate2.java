package edu.vsu.siuo;

import edu.vsu.siuo.domains.*;
import edu.vsu.siuo.domains.dto.ConditionsDto;
import edu.vsu.siuo.domains.dto.ShotDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.dto.TaskDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import edu.vsu.siuo.domains.enums.Types;
import edu.vsu.siuo.utils.Functions;

import java.util.*;

import static edu.vsu.siuo.utils.FuncGen.generateKnp;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;


//todo ПРИ ПС > 5-00 КУ И ШУ НЕ ИСПОЛЬЗУЮТСЯ И НЕ ВЫВОДЯТСЯ

public class Generate2 {

    public static List<TaskDto> generateTasks(int count) {
        List<TaskDto> taskDtos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            taskDtos.add(generateTask());
            taskDtos.get(i).setTaskNumber(i + 1);
            taskDtos.get(i).setTaskTopic(7);
        }
        return taskDtos;
    }

    public static TaskDto generateTask() {
        TaskDto taskDto = new TaskDto();
        ConditionsDto conditionsDto = generateConditionsForTask();

        taskDto.setProblemDto(conditionsDto);
        taskDto.setSolutionDto(generateSolution(conditionsDto));
        return taskDto;
    }

    private static OP generateOP() {
        OP op = new OP();

        op.setMainDirection(rand(1, 60) * 100);
        op.setX(rand(20000, 97000));
        op.setY(rand(20000, 97000));
        op.setH(rand(40, 180));

        return op;
    }

    private static ObjectPosition generateKNPfromOP(OP op) {
        int distanceFromOPtoKNP = rand(2500, 5200); // расстояние между ОП и КНП
//        int angleFromONtoKNP = Math.abs(op.getMainDirection() + (rand(0, 1) == 1 ? -1 : 1) * rand(50, 1450));
//        if (angleFromONtoKNP > 6000) angleFromONtoKNP = angleFromONtoKNP - 6000;

        int angleFromONtoKNP = 0;

        if (op.getMainDirection() > 5250 && op.getMainDirection() <= 6000) {
            angleFromONtoKNP = rand(5300, 5950);
        }
        if (op.getMainDirection() > 0 && op.getMainDirection() <= 750) {
            angleFromONtoKNP = rand(100, 700);
        }
        if (op.getMainDirection() > 750 && op.getMainDirection() <= 2250) {
            angleFromONtoKNP = rand(800, 2200);
        }
        if (op.getMainDirection() > 2250 && op.getMainDirection() <= 3750) {
            angleFromONtoKNP = rand(2300, 3700);
        }
        if (op.getMainDirection() > 3750 && op.getMainDirection() <= 5250) {
            angleFromONtoKNP = rand(3800, 5200);
        }

        return generateKnp(op.getX(), op.getY(), distanceFromOPtoKNP, angleFromONtoKNP);
    }

    private static Target generateTarget(OP op) {
        int angularMagnitude_target = (rand(0, 1) == 1 ? 1 : -1) * rand(1, 20); //угловая величина от низа цели, до ее верха (при наблюдении с кнп)

        // генерируем характер цели;
        Targets targetType = Targets.values()[rand(0, Targets.values().length - 1)];

        int angleFromKNPtoTarget = 0;

        if (op.getMainDirection() > 5250 && op.getMainDirection() <= 6000) {
            angleFromKNPtoTarget = rand(5300, 5950);
        }
        if (op.getMainDirection() > 0 && op.getMainDirection() <= 750) {
            angleFromKNPtoTarget = rand(100, 700);
        }
        if (op.getMainDirection() > 750 && op.getMainDirection() <= 2250) {
            angleFromKNPtoTarget = rand(800, 2200);
        }
        if (op.getMainDirection() > 2250 && op.getMainDirection() <= 3750) {
            angleFromKNPtoTarget = rand(2300, 3700);
        }
        if (op.getMainDirection() > 3750 && op.getMainDirection() <= 5250) {
            angleFromKNPtoTarget = rand(3800, 5200);
        }

//        int angleFromKNPtoTarget = Math.abs(op.getMainDirection() + (rand(0, 1) == 1 ? -1 : 1) * rand(50, 1450));
//        if (angleFromKNPtoTarget > 6000) angleFromKNPtoTarget = angleFromKNPtoTarget - 6000;

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
        target.setAngularMagnitudeTarget(angularMagnitude_target);
        return target;
    }


    public static ConditionsDto generateConditionsForTask() {
        // todo validation

        List<TaskDto> taskDtos = new ArrayList<>();

        OP op = generateOP();
        ObjectPosition knp = generateKNPfromOP(op);
        Target target = generateTarget(op);

        // fixme np_x, np_y = null?
        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        // fixme try generate again

        if (analysisResult.getDalTop() < 3000 || analysisResult.getPs() <= 580 || analysisResult.getDovTop() >= 380 || analysisResult.getDovTop() <= -380 || Math.abs(target.getAngleFromKNPtoTarget() - op.getMainDirection()) >= 750) {
            return generateConditionsForTask();
        }

//        // генерируем ГРП
//        Map<Integer, Map<String, Integer>> grp = new HashMap<>();
//        grp.put(0, new HashMap<>());
//        grp.put(1, new HashMap<>());
//        grp.put(2, new HashMap<>());
//
//        // дальность
//        //todo check it
//        // берем первый разряд dal_top и прибавляем rand(0, 1)
//        grp.get(1).put("D", ((int) analysisResult.getDalTop()) / 1000 + rand(0, 1));
//        grp.get(0).put("D", grp.get(1).get("D") - 2);
//        grp.get(2).put("D", grp.get(1).get("D") + 2);
//
//        for (int i = 0; i < 3; i++) {
//            // todo check it
//            grp.get(i).put("D", grp.get(i).get("D") * 1000);
//        }
//
//        if (rand(0, 1) == 1) { // поправка на дальность
//            grp.get(0).put("dD", rand(43, 260));
//            grp.get(1).put("dD", grp.get(0).get("dD") + rand(90, 180));
//            grp.get(2).put("dD", grp.get(1).get("dD") + rand(90, 180));
//        } else {
//            grp.get(0).put("dD", rand(-43, -260));
//            grp.get(1).put("dD", grp.get(0).get("dD") + rand(-60, -150));
//            grp.get(2).put("dD", grp.get(1).get("dD") + rand(-60, -150));
//        }
//
//        if (rand(0, 1) == 1) {  // доворот
//            grp.get(0).put("dd", rand(3, 15));
//            grp.get(1).put("dd", grp.get(0).get("dd") + rand(2, 8));
//            grp.get(2).put("dd", grp.get(1).get("dd") + rand(2, 8));
//        } else {
//            grp.get(0).put("dd", rand(-3, -15));
//            grp.get(1).put("dd", grp.get(0).get("dd") + rand(-2, -8));
//            grp.get(2).put("dd", grp.get(1).get("dd") + rand(-2, -8));
//        }

        GRP grp = new GRP((int) Math.round(analysisResult.getDalTop()));

        // исчисленная дальность и доворот
        List<Double> grp_count = grpCount(grp, analysisResult.getDalTop());
        double ddi = grp_count.get(0);


        double dal_isch = analysisResult.getDalTop() + ddi;

        Powers zaryd = null;
        if (dal_isch < 4800) {
            zaryd = (rand(3, 4) == 3 ? Powers.Power3 : Powers.Power4);
        } else if (dal_isch >= 4800 && dal_isch < 7400) {
            zaryd = (rand(1, 2) == 1 ? Powers.Power1 : Powers.Power2);
        } else if (dal_isch >= 7400) {
            zaryd = (rand(0, 1) == 1 ? Powers.Reduced : Powers.Full);
        }

        Map<Integer, ShotDto> shot = generateShot(target);


        ConditionsDto conditionsDto = new ConditionsDto(op, knp, target, shot);

        conditionsDto.setPower(zaryd);

        List<Integer> distance = List.of(grp.getDistance_1() / 1000, grp.getDistance_2() / 1000, grp.getDistance_3() / 1000);
        conditionsDto.setDistance(distance);

        List<Integer> range = List.of(grp.getDifDistance_1(), grp.getDifDistance_2(), grp.getDifDistance_3());
        conditionsDto.setRange(range);

        List<Integer> direction = List.of(grp.getTurn_1(), grp.getTurn_2(), grp.getTurn_3());
        conditionsDto.setDirection(direction);

        conditionsDto.setTarget(target);
        return conditionsDto;
    }

    public static SolutionDto generateSolution(ConditionsDto conditionsDto) {
        SolutionDto solutionDto = new SolutionDto();

        OP op = conditionsDto.getOp();
        Powers load = conditionsDto.getPower();
        Target target = conditionsDto.getTarget();
        ObjectPosition knp = conditionsDto.getKnp();
        Map<Integer, ShotDto> shot = conditionsDto.getShot();
        List<Integer> distance = conditionsDto.getDistance();
        List<Integer> range = conditionsDto.getRange();
        List<Integer> direction = conditionsDto.getDirection();

        double targetsFrontDu = target.getTargetsFrontDu();


        // Добавляем ГРП
        GRP grp = new GRP();
        grp.setDistance_1(distance.get(0) * 1000);
        grp.setDistance_2(distance.get(1) * 1000);
        grp.setDistance_3(distance.get(2) * 1000);
        grp.setDifDistance_1(range.get(0));
        grp.setDifDistance_2(range.get(1));
        grp.setDifDistance_3(range.get(2));
        grp.setTurn_1(direction.get(0));
        grp.setTurn_2(direction.get(1));
        grp.setTurn_3(direction.get(2));

        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        List<Double> grp_count = grpCount(grp, analysisResult.getDalTop());
        double ddi = grp_count.get(0);
        double dai = grp_count.get(1);

        Direction opDirection = analysisResult.getOpDirection();

        // уровень
        long urov = 0;
        if (analysisResult.getDalTop() != 0) {
            double target_h = knp.getH() + target.getDistanceFromKNPtoTarget() * target.getAngularMagnitudeTarget() * 1.0 / 1000; //высота цели
            double angularMagnitude_op = (target_h - op.getH()) / analysisResult.getDalTop() * 1000;

            urov = 3000 + Math.round(angularMagnitude_op);
        }

        double dalTop = analysisResult.getDalTop();

        double dal_isch = dalTop + ddi;
        double dov_isch = analysisResult.getDovTop() + dai;

        double ku = 0, shu = 0;

        if (dalTop != 0) {
            ku = round(target.getDistanceFromKNPtoTarget() / dalTop, 1);
            shu = Math.round(analysisResult.getPs() / dalTop * 100);
        }

        double gc_op = 0;
        double fcdu_op = 0;

        List<Double> ts_result = ts(load, dal_isch);
        double pric = ts_result.get(1);
        double dxt = ts_result.get(2);
        double vd = ts_result.get(3);
        pric = Math.round(pric);

        String vzr = null;
        if (target.getType().equals(Targets.PO) || target.getType().equals(Targets.RAP) || target.getType().equals(Targets.PTUR)) {
            vzr = "О";
        }
        if (target.getType().equals(Targets.PU) || target.getType().equals(Targets.BATR) || target.getType().equals(Targets.VZV)) {
            vzr = "О и Ф";
        }
        // todo fixme
        double fcm = (target.getTargetsFrontDu() * target.getDistanceFromKNPtoTarget()) / 1000;


        if (target.getTargetsDepth() > 200) target.setTargetsDepth(200);

        if (dxt == 0) dxt = 0.0001;
        int uu = 0;
        // скачок, УУ, УП
        if (fcm > 300 && target.getType().equals(Targets.PO) || fcm > 150 && target.getType().equals(Targets.PU)) {
            uu = 2;
        } else {
            uu = 1;
        }

        String uu_v = "";

        if (uu == 2) {
            uu_v = " установок " + uu + ',';
        }

        int rashod = 0;


        double cos_ps = 0;
        double muD = 0;
        double shu100 = 0;

        if (analysisResult.getPs() > 500) {
            double ps_rad = converseToRad(analysisResult.getPs());
            double sin_ps = round(Math.sin(ps_rad), 2);
            cos_ps = round(Math.cos(ps_rad), 2);
            muD = target.getDistanceFromKNPtoTarget() * 1.0 / 1000 * sin_ps;
            shu100 = sin_ps * 100000 / analysisResult.getDalTop();

            double pr1 = Math.abs(target.getTargetsFrontDu() * muD);
            double pr2 = Math.abs(target.getTargetsDepth() * cos_ps);

            double ugl1 = Math.abs(target.getTargetsFrontDu() * ku * cos_ps);
            double ugl2 = Math.abs(target.getTargetsDepth() * 1.0 / 100 * shu100);

            gc_op = pr1 + pr2;
            fcdu_op = ugl1 + ugl2;

            // todo check it
            target.setTargetsDepthOP(gc_op);
            target.setTargetsFrontDuOP(fcdu_op);

            solutionDto.setFDuOp((int) Math.round(fcdu_op));
            solutionDto.setGCOp((int) Math.round(gc_op));
        }

        double bat_veer;
        String bat_veer_v;
        if (target.getTargetsFrontDu() == 0) {
            bat_veer_v = "сосредоточенный";
        } else {
            //todo Спросить у Полковника про домножение на ku
            if (analysisResult.getPs() < 500) {
                bat_veer = (targetsFrontDu) / 6 * ku;
            } else {
                bat_veer = (fcdu_op) / 6;
            }
            bat_veer_v = modAngDash(bat_veer);
        }

        String skachok = "";
        int up;
        if (gc_op >= 100) {
            up = 3;
            skachok = " скачок " + Math.round(gc_op * 1.0 / 3 / dxt) + ',';
        } else {
            up = 1;
            skachok = "";
        }


        solutionDto.setDCt((int) Math.round(dalTop));
        solutionDto.setDeltaDCt((int) Math.round(ddi));
        solutionDto.setDCi((int) Math.round(dal_isch));

        solutionDto.setDeCt((int) Math.round(analysisResult.getDovTop()));
        solutionDto.setDeltaDeCt((int) Math.round(dai));
        solutionDto.setDeCi((int) Math.round(dov_isch));

        solutionDto.setKY((int) Math.round(ku));
        solutionDto.setShY((int) Math.round(shu));
        solutionDto.setDeltaX(dxt);

        solutionDto.setPs((int) Math.round(analysisResult.getPs()));

        solutionDto.setOp(analysisResult.getOpDirection());
        solutionDto.setVD((int) Math.round(vd));

        List<SolutionDto.TaskCommand> commands = new ArrayList<>();

        SolutionDto.TaskCommand firstCommand = new SolutionDto.TaskCommand();
        firstCommand.setDescription("«Дон», стой! Цель 21, «" + target.getType().getDescription() + "». ОФ, Взрыватель «" + vzr + "». Заряд " + load.getDescription() + ". Шкала тысячных, основному 1 сн. Огонь!");
        firstCommand.setPR((int) Math.round(pric));
        firstCommand.setYR(Math.round(urov));
        firstCommand.setDe((int) Math.round(dov_isch));
        firstCommand.setObservation(formatNabl(shot.get(0).getA(), shot.get(0).getType().getDescription(), shot.get(0).getF()));
        commands.add(firstCommand);

        int flag_k = 1; // для 1 команды батарее
        int vilka = 200 /*8*vd*/;

        int kol_nabl = 0;

        for (int i = 0; i < 7; i++) {
            if (shot.get(i) == null) shot.put(i, new ShotDto());
            if (shot.get(i).getType() != null && shot.get(i).getType().getDescription() != null && (!Types.EMPTY.equals(shot.get(i).getType()))) {
                kol_nabl = kol_nabl + 1;
            }
        }

        for (int i = 0; i < kol_nabl; i++) {

            int j = i + 2; // счетчик выстрелов

            // считываем наблюдения
            int alfa = shot.get(i).getA();

            String har = "";
            String har_next = "";

            if (shot.get(i).getType() != null && shot.get(i + 1).getType() != null) {
                har = shot.get(i).getType().name().toLowerCase();
                har_next = shot.get(i + 1).getType().name().toLowerCase(Locale.ROOT);
            } else if (i == kol_nabl - 1){
                //har = Types.TARGET_DESTROYED.name().toLowerCase(Locale.ROOT);
                har = shot.get(i).getType().name().toLowerCase();
            }

            Integer fr = shot.get(i).getF();

            String komand = "Огонь!";

            // последний 4 символ в строке

            double dD = 0;

            // поражение цели
            if (!har.equals("one_n") && !har.equals("one_p") && !har.equals("empty") && !har.equals("xz")) {


                if (gc_op < 100) {
                    if (har.equals("all_n") || har.equals("all_p")) dD = 50;
                    if (har.equals("pre_n") || har.equals("pre_p")) dD = 25;
                    if (har.equals("rav_n") || har.equals("rav_p")) dD = 25;
                } else {
                    if (har.equals("all_n") || har.equals("all_p")) dD = gc_op;
                    if (har.equals("pre_n") || har.equals("pre_p")) dD = (int) Math.round(2.0 / 3 * gc_op);
                    if (har.equals("rav_n") || har.equals("rav_p")) dD = (int) Math.round(1.0 / 2 * gc_op);
                }

                double koef_fr;

                if (targetsFrontDu != 0) {
                    koef_fr = fr / targetsFrontDu;
                } else {
                    koef_fr = 0;
                }


                if (fr != null && koef_fr >= 1.5 && analysisResult.getPs() <= 500) { // если есть фронт разрыва
                    double veer_raz = (targetsFrontDu - fr) / 6 * ku;
                    if (veer_raz < -0.99) komand = "Соединить к основному в " + modAngDash(veer_raz) + ". Огонь!";
                    else if (veer_raz > 0.99) komand = "Разделить от основного в " + modAngDash(veer_raz) + ". Огонь!";
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
            if (!har_next.equals("one_n") && !har_next.equals("one_p") && !har_next.equals("net") && !har_next.equals("xz")) {
                if (flag_k == 1) {
                    komand = "Батарее! Веер " + bat_veer_v + ',' + uu_v + skachok + " по 2 снаряда беглый. Огонь!";
                    flag_k = 0;
                }
            }

            double betta = 0;
            double pricel = 0;

            if (har.length() > 4) {
                String per_ned = String.valueOf(har.charAt(4));
                if (analysisResult.getPs() <= 500) { // по формулам
                    pricel = dD / dxt;
                    if (per_ned.equals("p")) pricel *= -1;

                    double kof_1 = -alfa * ku;
                    double kof_2 = dD / 100 * shu;
                    if ((opDirection.equals(Direction.LEFT) && per_ned.equals("n")) || (opDirection.equals(Direction.RIGHT) && per_ned.equals("p"))) {
                        kof_2 = -1 * kof_2;
                    }
                    betta = kof_1 + kof_2;
                } else { // по ПРК
                    double psRadian = converseToRad(analysisResult.getPs());
                    double dk = target.getDistanceFromKNPtoTarget();
                    double deltaDistFirstCircleAbs = Math.abs(alfa * 0.001 * dk / Functions.cosec(psRadian));
                    double deltaDistSecondCircleAbs = Math.abs(dD / Functions.sec(psRadian));

                    double bettaSecondCircle = Math.abs(dD * Math.tan(psRadian) / 0.001 / analysisResult.getDalTop() / Functions.sec(psRadian));
                    double bettaFirstCircle = Math.abs(alfa * 0.001 * dk / Functions.cosec(psRadian)) * Functions.cotan(psRadian) / (0.001 * analysisResult.getDalTop());


                    //Left-Right PRK values
                    if(analysisResult.getOpDirection() == Direction.LEFT) {
                        //first circle for left op position
                        if(alfa < 0) {
                            deltaDistFirstCircleAbs *= +1;
                            bettaFirstCircle *= +1;
                        } else {
                            deltaDistFirstCircleAbs *= -1;
                            bettaFirstCircle *= -1;
                        }
                        // second circle for left op position
                        if (per_ned.equals("n")) {
                            deltaDistSecondCircleAbs *= +1;
                            bettaSecondCircle *= -1;
                        } else {
                            deltaDistSecondCircleAbs *= -1;
                            bettaSecondCircle *= +1;
                        }
                    } else {
                        //first circle for right op position
                        if (alfa < 0) {
                            deltaDistFirstCircleAbs *= -1;
                            bettaFirstCircle *= +1;} else {
                            deltaDistFirstCircleAbs *= +1;
                            bettaFirstCircle *= -1;
                        }
                        // second circle for right op position
                        if (per_ned.equals("n")) {
                            deltaDistSecondCircleAbs *= +1;
                            bettaSecondCircle *= +1;
                        } else {
                            deltaDistSecondCircleAbs *= -1;
                            bettaSecondCircle *= -1;
                        }
                    }
//                    System.out.println("Дальность: первый круг = " + deltaDistFirstCircleAbs +"\t второй круг = " + deltaDistSecondCircleAbs);
//                    System.out.println("Угломер: первый круг = " + bettaFirstCircle + "\t второй круг = " + bettaSecondCircle + "\n\n\n");
                    betta = Math.round(bettaSecondCircle + bettaFirstCircle);
                    double deltaDist = deltaDistSecondCircleAbs + deltaDistFirstCircleAbs;

                    pricel = Math.round(deltaDist / dxt);
                    System.out.println();
                }
            }

            // формируем наблюдение
            String nablud = (i + 1 == kol_nabl ? "Цель подавлена" : formatNabl(shot.get(i + 1).getA(), shot.get(i + 1).getType().getDescription(), shot.get(i + 1).getF()));

            SolutionDto.TaskCommand command = new SolutionDto.TaskCommand();
            command.setDescription(komand);
            command.setPR((int) Math.round(pricel));
            command.setDe((int) Math.round(betta));
            command.setObservation(nablud);
            commands.add(command);
        }
        rashod += 6 * uu * up * 2;
        SolutionDto.TaskCommand lastCommand = new SolutionDto.TaskCommand();
        lastCommand.setDescription("Стой, записать! Цель 21, «" + target.getType().getDescription() + "». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход " + rashod + ". Я «Амур».");
        commands.add(lastCommand);

        solutionDto.setCommands(commands);

        return solutionDto;
    }
}