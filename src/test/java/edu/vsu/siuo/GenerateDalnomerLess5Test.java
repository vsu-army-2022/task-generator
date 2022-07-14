package edu.vsu.siuo;

import edu.vsu.siuo.domains.GeneratedShotResult;
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

class GenerateDalnomerLess5Test {

    @RepeatedTest(10000)
    @Disabled
    void repeatedTest() {
        GenerateNZRMore5.generateTask();
    }

    @Test
    void generateSolution1() {
        ConditionsDto conditionsDto = new ConditionsDto();

        OP op = new OP();
        op.setMainDirection(5500);
        op.setX(32791);
        op.setY(57569);
        op.setH(44);
        conditionsDto.setOp(op);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(34909);
        knp.setY(55901);
        knp.setH(126);
        conditionsDto.setKnp(knp);

        conditionsDto.setPower(Powers.Power2);
        conditionsDto.setDistance(List.of(4, 6, 8));
        conditionsDto.setRange(List.of(-62, -204, -281));
        conditionsDto.setDirection(List.of(6, 10, 12));

        Target target = new Target();
        target.setAngleFromKNPtoTarget(5485);
        target.setDistanceFromKNPtoTarget(3389);
        target.setAngularMagnitudeTarget(-12);
        target.setTargetsFrontDu(0);
        target.setTargetsDepth(0);
        target.setType(Targets.RAP);

        conditionsDto.setTarget(target);

        Map<Integer, ShotDto> shot = new HashMap<>();
        shot.put(0, new ShotDto(Types.ONE_P, -65));
        shot.put(1, new ShotDto(Types.ONE_P, -31));
        shot.put(2, new ShotDto(Types.ONE_N, +10));
        shot.put(3, new ShotDto(Types.ALL_N, +5, 76));
        shot.put(4, new ShotDto(Types.RAV_P, +5, 44));
        conditionsDto.setGeneratedShotResult(new GeneratedShotResult(shot));

        SolutionDto solutionDto = GenerateNZRLess5.generateSolution(conditionsDto);

        assertEquals(7835, solutionDto.getDCt());
        assertEquals(388, solutionDto.getDeltaDCt());
        assertEquals(8223, solutionDto.getDCi());
        assertEquals(-80, solutionDto.getDeCt());
        assertEquals(25, solutionDto.getDeltaDeCt());
        assertEquals(-55, solutionDto.getDeCi());
        assertEquals(0.40627537966552013, solutionDto.getKY());
        assertEquals(0.0, solutionDto.getShY());
        assertEquals(9, solutionDto.getPs());
        assertEquals(Direction.RIGHT, solutionDto.getOp());
        assertEquals(18, solutionDto.getDeltaX());
        assertEquals(19, solutionDto.getVD());

        solutionDto.getCommands();

        assertEquals("«Дон», стой! Цель 21, «пехота укрытая». ОФ, Взрыватель «О и Ф». Заряд У. Шкала тысячных, основному 1 сн. Огонь!", solutionDto.getCommands().get(0).getDescription());
        assertEquals(264, solutionDto.getCommands().get(0).getPR());
        assertEquals(3011, solutionDto.getCommands().get(0).getYR());
        assertEquals(-55, solutionDto.getCommands().get(0).getDe());
        assertEquals("Л65, «+»", solutionDto.getCommands().get(0).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(1).getDescription());
        assertEquals(-11, solutionDto.getCommands().get(1).getPR()); //должно быть 30
        assertNull(solutionDto.getCommands().get(1).getYR());
        assertEquals(+26, solutionDto.getCommands().get(1).getDe());
        assertEquals("Л31, «+»", solutionDto.getCommands().get(1).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(2).getDescription());
        assertEquals(-11, solutionDto.getCommands().get(2).getPR());
        assertNull(solutionDto.getCommands().get(2).getYR());
        assertEquals(+13, solutionDto.getCommands().get(2).getDe());
        assertEquals("П10, «-»", solutionDto.getCommands().get(2).getObservation());

        assertEquals("Батарее! Веер 0-03, установок 2, скачок 2, по 2 снаряда беглый. Огонь!", solutionDto.getCommands().get(3).getDescription()); //веер должен быть 0-04
        assertEquals(6, solutionDto.getCommands().get(3).getPR());
        assertNull(solutionDto.getCommands().get(3).getYR());
        assertEquals(-4, solutionDto.getCommands().get(3).getDe());
        assertEquals("П5, Все «-», Фр. 0-76", solutionDto.getCommands().get(3).getObservation());

        assertEquals("Соединить к основному в 0-02. Огонь!", solutionDto.getCommands().get(4).getDescription()); //Соединить к основному в 0-01. Огонь!
        assertEquals(6, solutionDto.getCommands().get(4).getPR());
        assertEquals(null, solutionDto.getCommands().get(4).getYR());
        assertEquals(-2, solutionDto.getCommands().get(4).getDe());
        assertEquals("П5, Равенство «+» и «-» от-но ДГЦ, Фр. 0-44", solutionDto.getCommands().get(4).getObservation());

        assertEquals("Огонь!", solutionDto.getCommands().get(5).getDescription());
        assertEquals(-3, solutionDto.getCommands().get(5).getPR());
        assertEquals(null, solutionDto.getCommands().get(5).getYR());
        assertEquals(-2, solutionDto.getCommands().get(5).getDe());
        assertEquals("Цель подавлена", solutionDto.getCommands().get(5).getObservation());

        assertEquals("Стой, записать! Цель 21, «пехота укрытая». «Лена»! «Амур» стрельбу по цели 21 закончил. Расход 219. Я «Амур».", solutionDto.getCommands().get(6).getDescription());
        assertNull(solutionDto.getCommands().get(6).getPR());
        assertNull(solutionDto.getCommands().get(6).getYR());
        assertNull(solutionDto.getCommands().get(6).getDe());
        assertEquals(null, solutionDto.getCommands().get(6).getObservation());

//        WordManager wordManager = new WordManager("C:\\Users\\denis\\Desktop", "TEST.docx");
//        List< TaskDto > list = new ArrayList<>();
//        TaskDto taskDto = new TaskDto();
//        taskDto.setSolutionDto(solutionDto);
//        taskDto.setProblemDto(conditionsDto);
//        list.add(taskDto);
//        wordManager.WriteNZRLess5(list);
    }
}