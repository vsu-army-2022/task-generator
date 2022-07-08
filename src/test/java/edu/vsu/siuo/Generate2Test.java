package edu.vsu.siuo;

import edu.vsu.siuo.domains.dto.ProblemDto;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.enums.Powers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Generate2Test {

    @Test
    void generateSolution() {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setOn(5900);
        problemDto.setXOp(30729);
        problemDto.setYOp(89961);
        problemDto.setHOp(146);
        problemDto.setXKnp(34255);
        problemDto.setYKnp(89512);
        problemDto.setHKnp(117);

        problemDto.setLoad(Powers.Reduced);
        problemDto.setDistance(List.of(6, 8, 10));
        problemDto.setRange(List.of(-52, -173, -286));
        problemDto.setDirection(List.of(-8, -10, -15));

        problemDto.setTargetType("открыто расположенные ЖС и ОС");
        problemDto.setAlphaC(5681);
        problemDto.setDK(4093);
        problemDto.setEpsC(-2);
        problemDto.setFDu(68);
        problemDto.setGC(152);

        SolutionDto solutionDto = Generate2.generateSolution(problemDto);

//        assertEquals(7602, solutionDto.getDCt());
//        assertEquals(-140, solutionDto.getDeltaDCt());
        assertEquals(7462, solutionDto.getDCi());
    }
}