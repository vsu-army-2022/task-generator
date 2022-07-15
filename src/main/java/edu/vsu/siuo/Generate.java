package edu.vsu.siuo;

import edu.vsu.siuo.domains.*;
import edu.vsu.siuo.domains.dto.ConditionsDto;
import edu.vsu.siuo.domains.dto.ShotDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;

import java.util.List;
import java.util.Map;

import static edu.vsu.siuo.utils.FuncGen.generateKnp;
import static edu.vsu.siuo.utils.Functions.*;
import static edu.vsu.siuo.utils.Utils.rand;
import static edu.vsu.siuo.utils.Utils.round;

public class Generate {
    static OP generateOP() {
        OP op = new OP();

        op.setMainDirection(rand(1, 60) * 100);
        op.setX(rand(20000, 97000));
        op.setY(rand(20000, 97000));
        op.setH(rand(40, 180));

        return op;
    }

    static Target generateTarget(OP op) {
        int angularMagnitude_target = (rand(0, 1) == 1 ? 1 : -1) * rand(1, 20); //угловая величина от низа цели, до ее верха (при наблюдении с кнп)

        // генерируем характер цели;
        Targets targetType = Targets.values()[rand(0, Targets.values().length - 1)];

        int angleFromKNPtoTarget = getAngelFromONToKnp(op);

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

    static ObjectPosition generateKNPFromOP(OP op) {
        int distanceFromOPtoKNP = rand(2500, 5200); // расстояние между ОП и КНП
//        int angleFromONtoKNP = Math.abs(op.getMainDirection() + (rand(0, 1) == 1 ? -1 : 1) * rand(50, 1450));
//        if (angleFromONtoKNP > 6000) angleFromONtoKNP = angleFromONtoKNP - 6000;

        int angleFromONtoKNP = getAngelFromONToKnp(op);

        return generateKnp(op.getX(), op.getY(), distanceFromOPtoKNP, angleFromONtoKNP);
    }

    private static int getAngelFromONToKnp(OP op) {
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
        return angleFromKNPtoTarget;
    }

    static ConditionsDto getConditionDto(AnalysisResult analysisResult, Target target, OP op, ObjectPosition knp) {
        GeneratedShotResult shot = generateShot(target);

        return getConditionsDto(analysisResult, target, op, knp, shot);
    }

    static ConditionsDto getConditionDtoDalnomer(AnalysisResult analysisResult, Target target, OP op, ObjectPosition knp) {
        GeneratedShotResult generatedShotResult = generateShotDalnomer(target);

        return getConditionsDto(analysisResult, target, op, knp, generatedShotResult);
    }

    private static ConditionsDto getConditionsDto(AnalysisResult analysisResult, Target target, OP op, ObjectPosition knp, GeneratedShotResult generatedShotResult) {
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

        ConditionsDto conditionsDto = new ConditionsDto(op, knp, target, generatedShotResult);

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

    static GRP getGrp(List<Integer> distance, List<Integer> range, List<Integer> direction) {
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
        return grp;
    }

    static SolutionDto generateLastCommand(SolutionDto solutionDto, Target target, int uu, int rashod, int up, List<SolutionDto.TaskCommand> commands) {
        rashod += 6 * uu * up * 2;
        SolutionDto.TaskCommand lastCommand = new SolutionDto.TaskCommand();
        lastCommand.setDescription("Стой, записать! Цель 21, «" + target.getType().getDescription() + "». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход " + rashod + ". Я «Амур».");
        commands.add(lastCommand);

        solutionDto.setCommands(commands);

        return solutionDto;
    }

    static double getBetta(String per_ned, int alfa, double ku, double dD, int shu, Direction opDirection) {
        double kof_1 = -alfa * ku;
        double kof_2 = 0;
        if (per_ned != null){
            kof_2 = dD / 100 * shu;
            if ((opDirection.equals(Direction.LEFT) && per_ned.equals("n")) || (opDirection.equals(Direction.RIGHT) && per_ned.equals("p"))) {
                kof_2 = -1 * kof_2;
            }
        }
        return kof_1 + kof_2;
    }

    static double getdD(double gc, String har, double dD) {
        if (gc < 100) {
            if (har.equals("all_n") || har.equals("all_p")) dD = 50;
            if (har.equals("pre_n") || har.equals("pre_p")) dD = 25;
            if (har.equals("rav_n") || har.equals("rav_p")) dD = 25;
        } else {
            if (har.equals("all_n") || har.equals("all_p")) dD = gc;
            if (har.equals("pre_n") || har.equals("pre_p")) dD = (int) Math.round(2.0 / 3 * gc);
            if (har.equals("rav_n") || har.equals("rav_p")) dD = (int) Math.round(1.0 / 2 * gc);
        }
        return dD;
    }

    static double getGcOp(AnalysisResult analysisResult, Target target) {
        double ps_rad = converseToRad(analysisResult.getPs());
        double sin_ps = round(Math.sin(ps_rad), 2);
        double cos_ps = round(Math.cos(ps_rad), 2);
        double muD = target.getDistanceFromKNPtoTarget() * 1.0 / 1000 * sin_ps;

        double pr1 = Math.abs(target.getTargetsFrontDu() * muD);
        double pr2 = Math.abs(target.getTargetsDepth() * cos_ps);
        return pr1 + pr2;
    }

    static void setFirstCommand(List<SolutionDto.TaskCommand> commands, Target target, String vzr, Powers load, double pric, long urov, double dov_isch, Map<Integer, ShotDto> shot) {
        SolutionDto.TaskCommand firstCommand = getFirstCommand(target, vzr, load, pric, urov, dov_isch);
        firstCommand.setObservation(formatNabl(shot.get(0).getA(), shot.get(0).getType().getDescription(), shot.get(0).getF()));
        commands.add(firstCommand);
    }

    static void setFirstCommandDalnimer(List<SolutionDto.TaskCommand> commands, Target target, String vzr, Powers load, double pric, long urov, double dov_isch, Map<Integer, ShotDto> shot) {
        SolutionDto.TaskCommand firstCommand = getFirstCommand(target, vzr, load, pric, urov, dov_isch);
        firstCommand.setObservation(formatNablDalnomer(shot.get(0).getA(), shot.get(0).getType(), shot.get(0).getRazr(), target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()));
        commands.add(firstCommand);
    }

    static SolutionDto.TaskCommand getFirstCommand(Target target, String vzr, Powers load, double pric, long urov, double dov_isch) {
        SolutionDto.TaskCommand firstCommand = new SolutionDto.TaskCommand();
        firstCommand.setDescription("«Дон», стой! Цель 21, «" + target.getType().getDescription() + "». ОФ, Взрыватель «" + vzr + "». Заряд " + load.getDescription() + ". Шкала тысячных, основному 1 сн. Огонь!");
        firstCommand.setPR((int) Math.round(pric));
        firstCommand.setYR(Math.round(urov));
        firstCommand.setDe((int) Math.round(dov_isch));
        return firstCommand;
    }

    static void generateNablud(Map<Integer, ShotDto> shot, List<SolutionDto.TaskCommand> commands, int kol_nabl, int i, String komand, double betta, double pricel) {
        String nablud = (i + 1 == kol_nabl ? "Цель подавлена" : formatNabl(shot.get(i + 1).getA(), shot.get(i + 1).getType().getDescription(), shot.get(i + 1).getF()));

        setCommandsForNabl(commands, komand, betta, pricel, nablud);
    }

    static void generateNabludForDal(Map<Integer, ShotDto> shot, List<SolutionDto.TaskCommand> commands, int kol_nabl, int i, String komand, double betta, double pricel, ConditionsDto conditionsDto) {
        String nablud;
        if (i == 0){
            nablud = conditionsDto.getGeneratedShotResult().getVse3v();
            komand = "3 снаряда, 20 секунд выстрел, огонь!";
        } else {
            nablud = (i + 1 == kol_nabl ? "Цель подавлена" : formatNabl(shot.get(i + 1).getA(), shot.get(i + 1).getType().getDescription(), shot.get(i + 1).getF()));
        }
        setCommandsForNabl(commands, komand, betta, pricel, nablud);
    }

    private static void setCommandsForNabl(List<SolutionDto.TaskCommand> commands, String komand, double betta, double pricel, String nablud) {
        SolutionDto.TaskCommand command = new SolutionDto.TaskCommand();
        command.setDescription(komand);
        command.setPR((int) Math.round(pricel));
        command.setDe((int) Math.round(betta));
        command.setObservation(nablud);
        commands.add(command);
    }
}
