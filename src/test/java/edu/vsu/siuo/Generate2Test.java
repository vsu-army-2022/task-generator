package edu.vsu.siuo;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Generate2Test {

    @RepeatedTest(10000)
    @Disabled
    void repeatedTest() {
        Generate2.generateTask();
    }

    @Test
    void generateSolution() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(4200);
        op.setX(84263);
        op.setY(44174);
        op.setH(99);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(86522);
        knp.setY(41546);
        knp.setH(41);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power1);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(196, 324, 415));
        conditionsDto.setDirection(List.of(6, 12, 16));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(3929);
        target.setDistanceFromKNPtoTarget(3502);
        target.setAngularMagnitudeTarget(8);
        target.setTargetsFrontDu(64);
        target.setTargetsDepth(62);
        target.setType(Targets.PU);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_P, +83));
        shot.put(1, new ShotDto(Types.ONE_N, -32));
        shot.put(2, new ShotDto(Types.ONE_N, -20));
        shot.put(3, new ShotDto(Types.ALL_P, +5, 96));
        shot.put(4, new ShotDto(Types.PRE_N, -7, 61));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = Generate2.generateSolution(conditionsDto);

        assertEquals(5530, solutionDto.getDCt());
        assertEquals(314, solutionDto.getDeltaDCt());
        assertEquals(5844, solutionDto.getDCi());
        assertEquals(350, solutionDto.getDeCt());
        assertEquals(12, solutionDto.getDeltaDeCt());
        assertEquals(362, solutionDto.getDeCi());
        // assertEquals(38, solutionDto.getFDuOp());
        // assertEquals(186, solutionDto.getGCOp());
        assertEquals(621, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(21, solutionDto.getDeltaX());
        assertEquals(14, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «пехота укрытая». ОФ, Взрыватель «О и Ф». Заряд 1. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(196, solutionDto.getCommands().get(0).getPR());
        assertEquals(2995, solutionDto.getCommands().get(0).getYR());
        assertEquals(362, solutionDto.getCommands().get(0).getDe());
        assertEquals("П83, «+»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(-16, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(-18, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л32, «-»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(7, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(4, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л20, «-»", solutionDto.getCommands().get(2).getObservation());

//        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals("Батарее! Веер 0-11, установок 2, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(4, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(4, solutionDto.getCommands().get(3).getDe());
        assertEquals("П5, Все «+», Фр. 0-96", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(-8, solutionDto.getCommands().get(4).getPR());
        assertEquals(0, solutionDto.getCommands().get(4).getYR());
        assertEquals(18, solutionDto.getCommands().get(4).getDe());
        assertEquals("Л7, Преобладание «-», Фр. 0-61", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(5, solutionDto.getCommands().get(5).getPR());
        assertEquals(0, solutionDto.getCommands().get(5).getYR());
        assertEquals(-10, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота укрытая». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals("", solutionDto.getCommands().get(6).getObservation());
    }

    @Test
    void generateSolution4() {
        //вариант 4
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(1500);
        op.setX(42524);
        op.setY(32747);
        op.setH(174);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(39770);
        knp.setY(36571);
        knp.setH(159);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power1);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(89, 257, 375));
        conditionsDto.setDirection(List.of(-3, -5, -9));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(986); //альфа цели
        target.setDistanceFromKNPtoTarget(2294);
        target.setAngularMagnitudeTarget(-7);
        target.setTargetsFrontDu(68);
        target.setTargetsDepth(99);
        target.setType(Targets.PO);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_P, -81));
        shot.put(1, new ShotDto(Types.ONE_N, -49));
        shot.put(2, new ShotDto(Types.ONE_N, +24));
        shot.put(3, new ShotDto(Types.ALL_N, +5, 96));
        shot.put(4, new ShotDto(Types.PRE_P, -9, 61));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = Generate2.generateSolution(conditionsDto);

        assertEquals(6005, solutionDto.getDCt());
        assertEquals(273, solutionDto.getDeltaDCt());
        assertEquals(6278, solutionDto.getDCi());
        assertEquals(254, solutionDto.getDeCt());
        assertEquals(-6, solutionDto.getDeltaDeCt());
        assertEquals(248, solutionDto.getDeCi());
        assertEquals(31, solutionDto.getFDuOp());
        assertEquals(181, solutionDto.getGCOp());
        assertEquals(768, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(20, solutionDto.getDeltaX());
        assertEquals(15, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «пехота». ОФ, Взрыватель «О». Заряд 1. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(219, solutionDto.getCommands().get(0).getPR());
        assertEquals(2995, solutionDto.getCommands().get(0).getYR());
        assertEquals(248, solutionDto.getCommands().get(0).getDe());
        assertEquals("Л81, «+»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(0, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(+46, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л49, «-»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(7, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(2, solutionDto.getCommands().get(2).getDe());
        assertEquals("П24, «-»", solutionDto.getCommands().get(2).getObservation());

//        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals("Батарее! Веер 0-05, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(0, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-13, solutionDto.getCommands().get(3).getDe());
        assertEquals("П5, Все «-», Фр. 1-09", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(6, solutionDto.getCommands().get(4).getPR());
        assertEquals(0, solutionDto.getCommands().get(4).getYR());
        assertEquals(-23, solutionDto.getCommands().get(4).getDe());
        assertEquals("Л7, Преобладание «-», Фр. 0-61", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-3, solutionDto.getCommands().get(5).getPR());
        assertEquals(0, solutionDto.getCommands().get(5).getYR());
        assertEquals(17, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals("", solutionDto.getCommands().get(6).getObservation());
    }

    @Test
    void generateSolution5() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(4700);
        op.setX(57498);
        op.setY(34955);
        op.setH(147);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(59847);
        knp.setY(31057);
        knp.setH(66);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power2);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(169, 324, 481));
        conditionsDto.setDirection(List.of(-9, -14, -20));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(4003);
        target.setDistanceFromKNPtoTarget(2666);
        target.setAngularMagnitudeTarget(-2);
        target.setTargetsFrontDu(0);
        target.setTargetsDepth(0);
        target.setType(Targets.PTUR);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_N, +75));
        shot.put(1, new ShotDto(Types.ONE_P, +49));
        shot.put(2, new ShotDto(Types.ONE_P, -17));
        shot.put(3, new ShotDto(Types.ALL_N, +11, 9));
        shot.put(4, new ShotDto(Types.PRE_P, -2, 6));

        conditionsDto.setShot(shot);

        SolutionDto solutionDto = Generate2.generateSolution(conditionsDto);

        assertEquals(6295, solutionDto.getDCt());
        assertEquals(377, solutionDto.getDeltaDCt());
        assertEquals(6672, solutionDto.getDCi());
        assertEquals(-44, solutionDto.getDeCt());
        assertEquals(-16, solutionDto.getDeltaDeCt());
        assertEquals(-60, solutionDto.getDeCi());
        assertEquals(653, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(14, solutionDto.getDeltaX());
        assertEquals(15, solutionDto.getVD());

        assertEquals("«Дон», стой! Цель 21, «птур в окопе». ОФ, Взрыватель «О». Заряд 2. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(310, solutionDto.getCommands().get(0).getPR());
        assertEquals(2986, solutionDto.getCommands().get(0).getYR());
        assertEquals(-60, solutionDto.getCommands().get(0).getDe());
        assertEquals("П75, «-»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(2, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(-43, solutionDto.getCommands().get(1).getDe());
        assertEquals("П49, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-11, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-5, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л17, «+»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер сосредоточенный, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(10, solutionDto.getCommands().get(3).getDe());
        assertEquals("П11, Все «-», Фр. 0-09", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(1, solutionDto.getCommands().get(4).getPR());
        assertNull(solutionDto.getCommands().get(4).getYR());
        assertEquals(-8, solutionDto.getCommands().get(4).getDe());
        assertEquals("Л2, Преобладание «+», Фр. 0-06", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(5).getPR());
        assertNull(solutionDto.getCommands().get(5).getYR());
        assertEquals(3, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «птур в окопе». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 39. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals("", solutionDto.getCommands().get(6).getObservation());
    }

    @Test
    void generateSolution2() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(4600);
        op.setX(77573);
        op.setY(26807);
        op.setH(53);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(75416);
        knp.setY(23947);
        knp.setH(136);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power1);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(167, 313, 469));
        conditionsDto.setDirection(List.of(-3, -5, -9));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(5019);
        target.setDistanceFromKNPtoTarget(3182);
        target.setAngularMagnitudeTarget(-12);
        target.setTargetsFrontDu(65);
        target.setTargetsDepth(57);
        target.setType(Targets.BATR);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_N, -66));
        shot.put(1, new ShotDto(Types.ONE_P, +34));
        shot.put(2, new ShotDto(Types.ONE_N, +12));
        shot.put(3, new ShotDto(Types.ALL_P, +13, 98));
        shot.put(4, new ShotDto(Types.PRE_P, -14, 63));

        conditionsDto.setShot(shot);

        SolutionDto solutionDto = Generate2.generateSolution(conditionsDto);

        assertEquals(5607, solutionDto.getDCt());
        assertEquals(307, solutionDto.getDeltaDCt());
        assertEquals(5914, solutionDto.getDCi());
        assertEquals(-187, solutionDto.getDeCt());
        assertEquals(-5, solutionDto.getDeltaDeCt());
        assertEquals(-192, solutionDto.getDeCi());
        assertEquals(38, solutionDto.getFDuOp());
        assertEquals(168, solutionDto.getGCOp());
        assertEquals(606, solutionDto.getPs());
        assertEquals(Direction.RIGHT, solutionDto.getOp());
        assertEquals(19, solutionDto.getDeltaX());
        assertEquals(14, solutionDto.getVD());

        assertEquals("«Дон», стой! Цель 21, «батарея». ОФ, Взрыватель «О и Ф». Заряд 1. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(200, solutionDto.getCommands().get(0).getPR());
        assertEquals(3008, solutionDto.getCommands().get(0).getYR());
        assertEquals(-192, solutionDto.getCommands().get(0).getDe());
        assertEquals("Л66, «-»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(2, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(53, solutionDto.getCommands().get(1).getDe());
        assertEquals("П34, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-28, solutionDto.getCommands().get(2).getDe());
        assertEquals("П12, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(3, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-1, solutionDto.getCommands().get(3).getDe());
        assertEquals("П13, Все «+», Фр. 0-98", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(-6, solutionDto.getCommands().get(4).getPR());
        assertNull(solutionDto.getCommands().get(4).getYR());
        assertEquals(-24, solutionDto.getCommands().get(4).getDe());
        assertEquals("Л14, Преобладание «+», Фр. 0-63", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-6, solutionDto.getCommands().get(5).getPR());
        assertNull(solutionDto.getCommands().get(5).getYR());
        assertEquals(-5, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «батарея». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals("", solutionDto.getCommands().get(6).getObservation());
    }
}