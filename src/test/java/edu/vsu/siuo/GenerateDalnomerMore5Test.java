package edu.vsu.siuo;

import edu.vsu.siuo.domains.GeneratedShotResult;
import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
import edu.vsu.siuo.domains.dto.ConditionsDto;
import edu.vsu.siuo.domains.dto.ShotDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import edu.vsu.siuo.domains.enums.Types;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenerateDalnomerMore5Test {


    @Test
    void generateTask1() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(4600);
        op.setX(36870);
        op.setY(63070);
        op.setH(61);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(34390);
        knp.setY(59198);
        knp.setH(52);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power1);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(-189, -286, -354));
        conditionsDto.setDirection(List.of(6, 12, 16));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(5175);
        target.setDistanceFromKNPtoTarget(3858);
        target.setAngularMagnitudeTarget(3);
        target.setTargetsFrontDu(57);
        target.setTargetsDepth(191);
        target.setType(Targets.PU);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.XZ, 5225));
        shot.put(1, new ShotDto(Types.ALL_P, -16, 88));
        shot.put(2, new ShotDto(Types.RAV_P,12, 51));
        conditionsDto.setGeneratedShotResult(new GeneratedShotResult(shot));
        conditionsDto.getGeneratedShotResult().setVse3v("5194,3959;52-00,3977; 52-05,4007");

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

        assertEquals(6805, solutionDto.getDCt());
        assertEquals(-303, solutionDto.getDeltaDCt());
        assertEquals(6502, solutionDto.getDCi());
        assertEquals(-96, solutionDto.getDeCt());
        assertEquals(14, solutionDto.getDeltaDeCt());
        assertEquals(-82, solutionDto.getDeCi());
        // assertEquals(38, solutionDto.getFDuOp());
        // assertEquals(186, solutionDto.getGCOp());
        assertEquals(671, solutionDto.getPs());
        assertEquals(Direction.RIGHT, solutionDto.getOp());
        assertEquals(19, solutionDto.getDeltaX());
        assertEquals(16, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «пехота укрытая». ОФ, Взрыватель «О и Ф». Заряд 1. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(231, solutionDto.getCommands().get(0).getPR());
        assertEquals(3000, solutionDto.getCommands().get(0).getYR());
        assertEquals(-82, solutionDto.getCommands().get(0).getDe());
        assertEquals("52-25, «?»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("3 снаряда, 20 секунд выстрел, огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(7, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(-23, solutionDto.getCommands().get(1).getDe());
        assertEquals("51-94,3959 52-00,3977 52-05, 4007", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Батарее! Веер 0-07, установок 2, скачек 4, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-2, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-23, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л16, Все «+», ФР. 0-88", solutionDto.getCommands().get(2).getObservation());

//        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals("Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(-10, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-12, solutionDto.getCommands().get(3).getDe());
        assertEquals("П12, Равенство «+» и «-» от-но ДГЦ, Фр. 0-51", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(-2, solutionDto.getCommands().get(4).getPR());
        assertNull(solutionDto.getCommands().get(4).getYR());
        assertEquals(-15, solutionDto.getCommands().get(4).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота укрытая». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 220. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals("", solutionDto.getCommands().get(6).getObservation());
    }


    @Test
    void generateTask() {
    }

    @Test
    void generateConditionsForTask() {
    }

    @Test
    void generateSolution() {
    }
}