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
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;


public class GenerateDalnomerLess5 extends Generate {

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
        OP op = generateOP();
        ObjectPosition knp = generateKNPFromOP(op);
        Target target = generateTarget(op);

        // fixme np_x, np_y = null?
        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        // fixme try generate again
        if (analysisResult.getDalTop() < 3000 || analysisResult.getPs() > 490 || analysisResult.getDovTop() >= 380 || analysisResult.getDovTop() <= -380 || Math.abs(target.getAngleFromKNPtoTarget() - op.getMainDirection()) >= 750) {
            return generateConditionsForTask();
        }

        return getConditionDtoDalnomer(analysisResult, target, op, knp);

    }

    public static SolutionDto generateSolution(ConditionsDto conditionsDto) {
        OP op = conditionsDto.getOp();
        Powers load = conditionsDto.getPower();
        Target target = conditionsDto.getTarget();
        ObjectPosition knp = conditionsDto.getKnp();
        Map<Integer, ShotDto> shot = conditionsDto.getGeneratedShotResult().getShot();
        List<Integer> distance = conditionsDto.getDistance();
        List<Integer> range = conditionsDto.getRange();
        List<Integer> direction = conditionsDto.getDirection();

        double targetsFrontDu = target.getTargetsFrontDu();

        // ?????????????????? ??????
        GRP grp = getGrp(distance, range, direction);

        AnalysisResult analysisResult = analyzePuo(target, knp, null, null, op);

        List<Double> grp_count = grpCount(grp, analysisResult.getDalTop());
        double ddi = grp_count.get(0);
        double dai = grp_count.get(1);

        Direction opDirection = analysisResult.getOpDirection();

        // ??????????????
        long urov = 0;
        if (analysisResult.getDalTop() != 0) {
            double target_h = knp.getH() + target.getDistanceFromKNPtoTarget() * target.getAngularMagnitudeTarget() * 1.0 / 1000; //???????????? ????????
            double angularMagnitude_op = (target_h - op.getH()) / analysisResult.getDalTop() * 1000;

            urov = 3000 + Math.round(angularMagnitude_op);
        }

        double dalTop = analysisResult.getDalTop();

        double dal_isch = dalTop + ddi;
        double dov_isch = analysisResult.getDovTop() + dai;

        double ku = 0;
        int shu = 0;
        if (dalTop != 0) {
            ku = target.getDistanceFromKNPtoTarget()*1.0 / dalTop;
            shu = (int) Math.round(analysisResult.getPs() / dalTop * 100);
        }

        List<Double> ts_result = ts(load, dal_isch);
        double pric = ts_result.get(1);
        double dxt = ts_result.get(2);
        double vd = ts_result.get(3);

        String vzr = null;
        if (target.getType().equals(Targets.PO) || target.getType().equals(Targets.RAP) || target.getType().equals(Targets.PTUR)) {
            vzr = "??";
        }
        if (target.getType().equals(Targets.PU) || target.getType().equals(Targets.BATR) || target.getType().equals(Targets.VZV)) {
            vzr = "?? ?? ??";
        }
        // todo fixme
        double fcm = (target.getTargetsFrontDu() * target.getDistanceFromKNPtoTarget()) / 1000;

        if (target.getTargetsDepth() > 200) target.setTargetsDepth(200);

        if (dxt == 0) dxt = 0.0000001;

        int uu;
        // ????????????, ????, ????
        if (fcm > 300 && target.getType().equals(Targets.PO) || fcm > 150 && target.getType().equals(Targets.PU)) {
            uu = 2;
        } else {
            uu = 1;
        }

        String uu_v = "";

        if (uu == 2) {
            uu_v = " ?????????????????? " + uu + ',';
        }

        String bat_veer_v;
        if (target.getTargetsFrontDu() == 0) {
            bat_veer_v = "??????????????????????????????";
        } else {
            //todo ???????????????? ?? ???????????????????? ?????? ???????????????????? ???? ku
            double bat_veer = (targetsFrontDu) / 6 * ku;
            bat_veer_v = modAngDash(bat_veer);
        }

        String skachok;
        int up;
        double gc_op = getGcOp(analysisResult, target);
        if (gc_op >= 100) {
            up = 3;
            skachok = " ???????????? " + Math.round(gc_op * 1.0 / 3 / dxt) + ',';
        } else {
            up = 1;
            skachok = "";
        }

        SolutionDto solutionDto = new SolutionDto();

        solutionDto.setDCt((int) Math.round(dalTop));
        solutionDto.setDeltaDCt((int) Math.round(ddi));
        solutionDto.setDCi((int) Math.round(dal_isch));

        solutionDto.setDeCt((int) Math.round(analysisResult.getDovTop()));
        solutionDto.setDeltaDeCt((int) Math.round(dai));
        solutionDto.setDeCi((int) Math.round(dov_isch));

        solutionDto.setKY(ku);
        solutionDto.setShY(Math.round(shu));
        solutionDto.setDeltaX(dxt);

        solutionDto.setPs((int) Math.round(analysisResult.getPs()));

        solutionDto.setOp(analysisResult.getOpDirection());
        solutionDto.setVD((int) Math.round(vd));

        List<SolutionDto.TaskCommand> commands = new ArrayList<>();

        setFirstCommandDalnimer(commands, target, vzr, load, pric, urov, dov_isch, shot);

        int flag_k = 1; // ?????? 1 ?????????????? ??????????????

        int kol_nabl = 0;

        for (int i = 0; i < 7; i++) {
            if (shot.get(i) == null) shot.put(i, new ShotDto());
            if (shot.get(i).getType() != null && shot.get(i).getType().getDescription() != null && (!Types.EMPTY.equals(shot.get(i).getType()))) {
                kol_nabl = kol_nabl + 1;
            }
        }

        int rashod = 0;
        for (int i = 0; i < kol_nabl; i++) {
            // ?????????????????? ????????????????????
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

            String komand = "??????????!";

            // ?????????????????? 4 ???????????? ?? ????????????

            double dD = 0;

            // ?????????????????? ????????
            if (!har.equals("one_n") && !har.equals("one_p") && !har.equals("empty") && !har.equals("xz")) {

                dD = getdD(target.getTargetsDepth(), har, dD);

//                double koef_fr;
//
//                if (targetsFrontDu != 0) {
//                    koef_fr = fr / targetsFrontDu;
//                } else {
//                    koef_fr = 0;
//                }

                //if (fr != null && koef_fr >= 1.5) { // ???????? ???????? ?????????? ??????????????
                if (!target.getType().equals(Targets.RAP) && !target.getType().equals(Targets.PTUR)) {
                    double veer_raz = (targetsFrontDu - fr) / 6 * ku;
                    if (veer_raz < -0.99) komand = "?????????????????? ?? ?????????????????? ?? " + modAngDash(veer_raz) + ". ??????????!";
                    else if (veer_raz > 0.99) komand = "?????????????????? ???? ?????????????????? ?? " + modAngDash(veer_raz) + ". ??????????!";
                }
                //}

                rashod += 6 * uu * up * 2;
            } else {
                dD = shot.get(i).getRazr();
                rashod+=2;
            }


            // 1 ?????????????? ???? ?????????????????? ??????????????
            if (!har_next.equals("one_n") && !har_next.equals("one_p") && !har_next.equals("net") && !har_next.equals("xz")) {
                if (flag_k == 1) {
                    komand = "??????????????! ???????? " + bat_veer_v + ',' + uu_v + skachok + " ???? 2 ?????????????? ????????????. ??????????!";
                    flag_k = 0;
                }
            }

            double betta = 0;
            double pricel = 0;
            if (har.length() > 4) {
                String per_ned = String.valueOf(har.charAt(4));
                pricel = dD / dxt;
                if (per_ned.equals("p")) pricel *= -1;

                betta = getBetta(per_ned, alfa, ku, dD, shu, opDirection);
            }

            // ?????????????????? ????????????????????
            generateNabludForDal(shot, commands, kol_nabl, i, komand, betta, pricel, conditionsDto);
        }

        return generateLastCommand(solutionDto, target, uu, rashod, up, commands);
    }
}