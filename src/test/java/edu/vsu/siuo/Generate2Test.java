package edu.vsu.siuo;

import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
import edu.vsu.siuo.domains.dto.ConditionsDto;
import edu.vsu.siuo.domains.dto.ShotDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.dto.TaskDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import edu.vsu.siuo.domains.enums.Types;
import edu.vsu.siuo.word.WordManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Generate2Test {

    @RepeatedTest(10000)
    @Disabled
    void repeatedTest() {
        GenerateNZRMore5.generateTask();
    }

    @Test
    void generateTrueSolution1() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(1600);
        op.setX(52577);
        op.setY(47917);
        op.setH(154);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(54025);
        knp.setY(50082);
        knp.setH(79);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power4);
        conditionsDto.setDistance(List.of(2, 4, 6));
        conditionsDto.setRange(List.of(87, 267, 413));
        conditionsDto.setDirection(List.of(13, 20, 27));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(2170);
        target.setDistanceFromKNPtoTarget(2670);
        target.setAngularMagnitudeTarget(1);
        target.setTargetsFrontDu(43);
        target.setTargetsDepth(47);
        target.setType(Targets.VZV);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_N, +70));
        shot.put(1, new ShotDto(Types.ONE_P, +47));
        shot.put(2, new ShotDto(Types.ONE_N, +5));
        shot.put(3, new ShotDto(Types.ALL_N, -12, 85));
        shot.put(4, new ShotDto(Types.PRE_N, 16, 34));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

        assertEquals(4213, solutionDto.getDCt());
        assertEquals(305, solutionDto.getDeltaDCt());
        assertEquals(4518, solutionDto.getDCi());
        assertEquals(-38, solutionDto.getDeCt());
        assertEquals(22, solutionDto.getDeltaDeCt());
        assertEquals(-16, solutionDto.getDeCi());
        assertEquals(27, solutionDto.getFDuOp());
        assertEquals(105, solutionDto.getGCOp());
        assertEquals(608, solutionDto.getPs());
        assertEquals(Direction.RIGHT, solutionDto.getOp());
        assertEquals(8.73668757667123, solutionDto.getDeltaX()); // 8.9 должно быть
        assertEquals(25, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «взвод артиллерийский». ОФ, Взрыватель «О и Ф». Заряд 4. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(352, solutionDto.getCommands().get(0).getPR());
        assertEquals(2983, solutionDto.getCommands().get(0).getYR());
        assertEquals(-16, solutionDto.getCommands().get(0).getDe());
        assertEquals("П70, «-»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(31, solutionDto.getCommands().get(1).getPR()); //должно быть 30
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(-7, solutionDto.getCommands().get(1).getDe());
        assertEquals("П47, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-38, solutionDto.getCommands().get(2).getDe());
        assertEquals("П5, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-05, скачок 4, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription()); //веер должен быть 0-04
        assertEquals(6, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(5, solutionDto.getCommands().get(3).getDe());
        assertEquals("Л12, Все «-», Фр. 0-85", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(8, solutionDto.getCommands().get(4).getPR());
        assertEquals(null, solutionDto.getCommands().get(4).getYR());
        assertEquals(21, solutionDto.getCommands().get(4).getDe());
        assertEquals("П16, Преобладание «-», Фр. 0-34", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(9, solutionDto.getCommands().get(5).getPR());
        assertEquals(null, solutionDto.getCommands().get(5).getYR());
        assertEquals(2, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «взвод артиллерийский». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());
    }

    @Test
    void generateTrueSolution2() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(1500);
        op.setX(77612);
        op.setY(39556);
        op.setH(180);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(75515);
        knp.setY(42230);
        knp.setH(168);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power3);
        conditionsDto.setDistance(List.of(3, 5, 7));
        conditionsDto.setRange(List.of(-50, -176, -305));
        conditionsDto.setDirection(List.of(-7, -13, -17));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(1013);
        target.setDistanceFromKNPtoTarget(2346);
        target.setAngularMagnitudeTarget(19);
        target.setTargetsFrontDu(77);
        target.setTargetsDepth(100);
        target.setType(Targets.BATR);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.XZ, +91));
        shot.put(1, new ShotDto(Types.ONE_P, -34));
        shot.put(2, new ShotDto(Types.ONE_N, -25));
        shot.put(3, new ShotDto(Types.ALL_P, -8, 127));
        shot.put(4, new ShotDto(Types.RAV_P, +14, 83));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

        assertEquals(4816, solutionDto.getDCt());
        assertEquals(-155, solutionDto.getDeltaDCt());
        assertEquals(4661, solutionDto.getDCi());
        assertEquals(190, solutionDto.getDeCt());
        assertEquals(-12, solutionDto.getDeltaDeCt());
        assertEquals(178, solutionDto.getDeCi());
        assertEquals(43, solutionDto.getFDuOp());
        assertEquals(193, solutionDto.getGCOp());
        assertEquals(677, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(15.307250119591972, solutionDto.getDeltaX());
        assertEquals(25, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «батарея». ОФ, Взрыватель «О и Ф». Заряд 3. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(250, solutionDto.getCommands().get(0).getPR());
        assertEquals(3007, solutionDto.getCommands().get(0).getYR());
        assertEquals(178, solutionDto.getCommands().get(0).getDe());
        assertEquals("П91, «?»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(-9, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(-34, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л34, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-7, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(40, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л25, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-07, скачок 4, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription()); //веер должен быть 0-04
        assertEquals(7, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-4, solutionDto.getCommands().get(3).getDe());
        assertEquals("Л8, Все «+», Фр. 1-27", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(-9, solutionDto.getCommands().get(4).getPR());
        assertEquals(null, solutionDto.getCommands().get(4).getYR());
        assertEquals(29, solutionDto.getCommands().get(4).getDe());
        assertEquals("П14, Равенство «+» и «-» от-но ДГЦ, Фр. 0-83", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-6, solutionDto.getCommands().get(5).getPR());
        assertEquals(null, solutionDto.getCommands().get(5).getYR());
        assertEquals(8, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «батарея». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());
    }

    @Test
    void generateTrueSolution3() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(1300);
        op.setX(51805);
        op.setY(47738);
        op.setH(46);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(49903);
        knp.setY(50008);
        knp.setH(74);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power4);
        conditionsDto.setDistance(List.of(2, 4, 6));
        conditionsDto.setRange(List.of(119, 244, 378));
        conditionsDto.setDirection(List.of(-8, -11, -13));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(1006);
        target.setDistanceFromKNPtoTarget(2239);
        target.setAngularMagnitudeTarget(3);
        target.setTargetsFrontDu(132);
        target.setTargetsDepth(70);
        target.setType(Targets.PO);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_N, -94));
        shot.put(1, new ShotDto(Types.ONE_P, -46));
        shot.put(2, new ShotDto(Types.ONE_N, -14));
        shot.put(3, new ShotDto(Types.ALL_N, +19, 201));
        shot.put(4, new ShotDto(Types.PRE_P, +7, 120));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

        assertEquals(4290, solutionDto.getDCt());
        assertEquals(282, solutionDto.getDeltaDCt());
        assertEquals(4572, solutionDto.getDCi());
        assertEquals(378, solutionDto.getDeCt());
        assertEquals(-12, solutionDto.getDeltaDeCt());
        assertEquals(366, solutionDto.getDeCi());
        assertEquals(61, solutionDto.getFDuOp());
        assertEquals(245, solutionDto.getGCOp());
        assertEquals(672, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(8.844544664114485, solutionDto.getDeltaX());
        assertEquals(25, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «пехота». ОФ, Взрыватель «О». Заряд 4. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(358, solutionDto.getCommands().get(0).getPR());
        assertEquals(3008, solutionDto.getCommands().get(0).getYR());
        assertEquals(366, solutionDto.getCommands().get(0).getDe());
        assertEquals("Л94, «-»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(33, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(7, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л46, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(33, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л14, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-10, скачок 9, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription()); //скачок должен быть 7
        assertEquals(7, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-2, solutionDto.getCommands().get(3).getDe());
        assertEquals("П19, Все «-», Фр. 2-01", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(18, solutionDto.getCommands().get(4).getPR()); //должно быть 14
        assertEquals(null, solutionDto.getCommands().get(4).getYR());
        assertEquals(-45, solutionDto.getCommands().get(4).getDe()); //должно быть -38
        assertEquals("П7, Преобладание «+», Фр. 1-20", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-15, solutionDto.getCommands().get(5).getPR()); //-13
        assertEquals(null, solutionDto.getCommands().get(5).getYR());
        assertEquals(22, solutionDto.getCommands().get(5).getDe()); //17
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());

        WordManager wordManager = new WordManager("C:\\Users\\denis\\Desktop", "TEST.docx");
        List<TaskDto> list = new ArrayList<>();
        TaskDto taskDto = new TaskDto();
        taskDto.setSolutionDto(solutionDto);
        taskDto.setProblemDto(conditionsDto);
        list.add(taskDto);
        wordManager.WriteNZRMore5(list);
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

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

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
        assertEquals(21.219273308251392, solutionDto.getDeltaX());
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
        assertEquals(-20, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л32, «-»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(7, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(5, solutionDto.getCommands().get(2).getDe());
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
        shot.put(3, new ShotDto(Types.ALL_N, +5, 109));
        shot.put(4, new ShotDto(Types.PRE_P, -9, 79));
        conditionsDto.setShot(shot);

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

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
        assertEquals(20.388435674026884, solutionDto.getDeltaX());
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
        assertEquals(+45, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л49, «-»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(7, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(1, solutionDto.getCommands().get(2).getDe());
        assertEquals("П24, «-»", solutionDto.getCommands().get(2).getObservation());

//        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals("Батарее! Веер 0-05, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(0, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-12, solutionDto.getCommands().get(3).getDe());
        assertEquals("П5, Все «-», Фр. 1-09", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(6, solutionDto.getCommands().get(4).getPR());
        assertEquals(null, solutionDto.getCommands().get(4).getYR());
        assertEquals(-23, solutionDto.getCommands().get(4).getDe());
        assertEquals("Л9, Преобладание «+», Фр. 0-79", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-3, solutionDto.getCommands().get(5).getPR());
        assertEquals(null, solutionDto.getCommands().get(5).getYR());
        assertEquals(17, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 111. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());
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

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

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
        assertEquals(-45, solutionDto.getCommands().get(1).getDe());
        assertEquals("П49, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-11, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-6, solutionDto.getCommands().get(2).getDe());
        assertEquals("Л17, «+»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер сосредоточенный, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(11, solutionDto.getCommands().get(3).getDe());
        assertEquals("П11, Все «-», Фр. 0-09", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(4).getDescription());
        assertEquals(1, solutionDto.getCommands().get(4).getPR());
        assertNull(solutionDto.getCommands().get(4).getYR());
        assertEquals(-9, solutionDto.getCommands().get(4).getDe());
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
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());
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

        SolutionDto solutionDto = GenerateNZRMore5.generateSolution(conditionsDto);

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
        assertEquals(19.56936443773961, solutionDto.getDeltaX());
        assertEquals(15, solutionDto.getVD());

        assertEquals("«Дон», стой! Цель 21, «батарея». ОФ, Взрыватель «О и Ф». Заряд 1. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(200, solutionDto.getCommands().get(0).getPR());
        assertEquals(3008, solutionDto.getCommands().get(0).getYR());
        assertEquals(-192, solutionDto.getCommands().get(0).getDe());
        assertEquals("Л66, «-»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(2, solutionDto.getCommands().get(1).getPR());
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(51, solutionDto.getCommands().get(1).getDe());
        assertEquals("П34, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-1, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(-26, solutionDto.getCommands().get(2).getDe());
        assertEquals("П12, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-06, скачок 3, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription());
        assertEquals(3, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(0, solutionDto.getCommands().get(3).getDe());
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
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());
    }
}