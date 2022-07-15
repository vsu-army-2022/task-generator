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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static edu.vsu.siuo.utils.FuncGen.generateKnp;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Functions.formatNabl;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;

public class GenerateDalnomerMore5 extends Generate {

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

    public static ConditionsDto generateConditionsForTask() {
        // todo validation

        OP op = generateOP();
        ObjectPosition knp = generateKNPFromOP(op);
        Target target = generateTarget(op);

        // fixme np_x, np_y = null?
        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        // fixme try generate again

        if (analysisResult.getDalTop() < 3000 || analysisResult.getPs() <= 580 || analysisResult.getDovTop() >= 380 || analysisResult.getDovTop() <= -380 || Math.abs(target.getAngleFromKNPtoTarget() - op.getMainDirection()) >= 750) {
            return generateConditionsForTask();
        }

        return getConditionDtoDalnomer(analysisResult, target, op, knp);
    }

    public static SolutionDto generateSolution(ConditionsDto conditionsDto) {
        SolutionDto solutionDto = new SolutionDto();

        OP op = conditionsDto.getOp();
        Powers load = conditionsDto.getPower();
        Target target = conditionsDto.getTarget();
        ObjectPosition knp = conditionsDto.getKnp();
        Map<Integer, ShotDto> shot = conditionsDto.getGeneratedShotResult().getShot();
        List<Integer> distance = conditionsDto.getDistance();
        List<Integer> range = conditionsDto.getRange();
        List<Integer> direction = conditionsDto.getDirection();

        double targetsFrontDu = target.getTargetsFrontDu();

        // Добавляем ГРП
        GRP grp = getGrp(distance, range, direction);

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

        double ku = 0;
        int shu = 0;

        if (dalTop != 0) {
            ku = target.getDistanceFromKNPtoTarget() * 1.0 / dalTop;
            shu = (int) Math.round(analysisResult.getPs() / dalTop * 100);
        }

        List<Double> ts_result = ts(load, dal_isch);
        double pric = ts_result.get(1);
        double dxt = ts_result.get(2);
        double vd = ts_result.get(3);

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
        int uu;
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

        double gc_op = 0;
        double fcdu_op = 0;
        if (analysisResult.getPs() > 500) {
            double ps_rad = converseToRad(analysisResult.getPs());
            double sin_ps = round(Math.sin(ps_rad), 2);
            double cos_ps = round(Math.cos(ps_rad), 2);
            double shu100 = sin_ps * 100000 / analysisResult.getDalTop();

            double ugl1 = Math.abs(target.getTargetsFrontDu() * ku * cos_ps);
            double ugl2 = Math.abs(target.getTargetsDepth() * 1.0 / 100 * shu100);

            gc_op = getGcOp(analysisResult, target);
            fcdu_op = ugl1 + ugl2;

            // todo check it
            target.setTargetsDepthOP(gc_op);
            target.setTargetsFrontDuOP(fcdu_op);

            solutionDto.setFDuOp((int) Math.round(fcdu_op));
            solutionDto.setGCOp((int) Math.round(gc_op));
        }

        String bat_veer_v;
        if (target.getTargetsFrontDu() == 0) {
            bat_veer_v = "сосредоточенный";
        } else {
            //todo Спросить у Полковника про домножение на ku
            double bat_veer;
            if (analysisResult.getPs() < 500) {
                bat_veer = (targetsFrontDu) / 6 * ku;
            } else {
                bat_veer = (fcdu_op) / 6;
            }
            bat_veer_v = modAngDash(bat_veer);
        }

        String skachok;
        int up;
        if (gc_op >= 100) {
            up = 3;
            skachok = " скачок " + Math.round(gc_op / 3 / dxt) + ',';
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

        setFirstCommandDalnimer(commands, target, vzr, load, pric, urov, dov_isch, shot);

        int flag_k = 1; // для 1 команды батарее

        int kol_nabl = 0;

        for (int i = 0; i < 7; i++) {
            if (shot.get(i) == null) shot.put(i, new ShotDto());
            if (shot.get(i).getType() != null && shot.get(i).getType().getDescription() != null && (!Types.EMPTY.equals(shot.get(i).getType()))) {
                kol_nabl = kol_nabl + 1;
            }
        }

        int rashod = 0;
        for (int i = 0; i < kol_nabl; i++) {
            // считываем наблюдения
            int alfa = shot.get(i).getA();

            String har = "";
            String har_next = "";

            if (shot.get(i).getType() != null && shot.get(i + 1).getType() != null) {
                har = shot.get(i).getType().name().toLowerCase();
                har_next = shot.get(i + 1).getType().name().toLowerCase(Locale.ROOT);
            } else if (i == kol_nabl - 1) {
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

//                double koef_fr;
//
//                if (targetsFrontDu != 0) {
//                    koef_fr = fr / targetsFrontDu;
//                } else {
//                    koef_fr = 0;
//                }

                rashod += 6 * uu * up * 2;
            } else {
                dD = shot.get(i).getRazr();

                rashod += 2;
            }


            // 1 команда на поражение батарее
            if (!har_next.equals("one_n") && !har_next.equals("one_p") && !har_next.equals("net") && !har_next.equals("xz")) {
                if (flag_k == 1) {
                    komand = "Батарее! Веер " + bat_veer_v + ',' + uu_v + skachok + " по 2 снаряда беглый. Огонь!";
                    flag_k = 0;
                }
            }

            String per_ned = "";

            if (har.length() > 4) {
                per_ned = String.valueOf(har.charAt(4));
            }
            double betta;
            double pricel;
            if (analysisResult.getPs() <= 500) { // по формулам
                pricel = dD / dxt;
                if (per_ned.equals("p")) pricel *= -1;
                betta = getBetta(per_ned, alfa, ku, dD, shu, opDirection);
            } else { // по ПРК
                double psRadian = converseToRad(analysisResult.getPs());
                double dk = target.getDistanceFromKNPtoTarget();
                double deltaDistFirstCircleAbs = Math.abs(alfa * 0.001 * dk / Functions.cosec(psRadian));
                double deltaDistSecondCircleAbs = Math.abs(dD / Functions.sec(psRadian));

                double bettaSecondCircle = Math.abs(dD * Math.tan(psRadian) / 0.001 / analysisResult.getDalTop() / Functions.sec(psRadian));
                double bettaFirstCircle = Math.abs(alfa * 0.001 * dk / Functions.cosec(psRadian)) * Functions.cotan(psRadian) / (0.001 * analysisResult.getDalTop());


                //Left-Right PRK values
                if (analysisResult.getOpDirection() == Direction.LEFT) {
                    //first circle for left op position
                    if (alfa < 0) {
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
                        bettaFirstCircle *= +1;
                    } else {
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
            }


            // формируем наблюдение
            generateNabludForDal(shot, commands, kol_nabl, i, komand, betta, pricel, conditionsDto);
        }

        return generateLastCommand(solutionDto, target, uu, rashod, up, commands);
    }
}
