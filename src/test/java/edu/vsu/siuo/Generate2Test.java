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
}