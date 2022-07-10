package edu.vsu.siuo;

import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
import edu.vsu.siuo.domains.dto.ConditionsDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


        SolutionDto solutionDto = Generate2.generateSolution(conditionsDto);

        assertEquals(5529, solutionDto.getDCt());
        // deltaD?
        assertEquals(5843, solutionDto.getDeCi());
        assertEquals(350, solutionDto.getDeCt());
        // ?
        assertEquals(362, solutionDto.getDeCi());
        assertEquals(38, solutionDto.getFDuOp());
        assertEquals(186, solutionDto.getGCOp());
        assertEquals(621, solutionDto.getPs());
        assertEquals(Direction.LEFT, solutionDto.getOp());
        assertEquals(21, solutionDto.getDeltaX());
        assertEquals(14, solutionDto.getVD());


    }
}