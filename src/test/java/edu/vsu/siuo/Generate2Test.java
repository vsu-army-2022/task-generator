package edu.vsu.siuo;

import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.dto.SolutionDto;
import edu.vsu.siuo.domains.enums.Powers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Generate2Test {

//    @Test
//    void generateSolution() {
//        ProblemDto problemDto = new ProblemDto();
//        problemDto.setOn(4200);
//        problemDto.setXOp(84263);
//        problemDto.setYOp(44174);
//        problemDto.setOp(new OP());
//        problemDto.setHOp(99);
//        problemDto.setXKnp(86522);
//        problemDto.setYKnp(41546);
//        problemDto.setHKnp(41);
//
//        problemDto.setLoad(Powers.Power1);
//        problemDto.setDistance(List.of(4, 6, 8));
//        problemDto.setRange(List.of(196, 324, 415));
//        problemDto.setDirection(List.of(6, 12, 16));
//
//        //problemDto.setTargetType( "ЖС и ОС, расположенные в окопах (траншеях)");
//        problemDto.setAlphaC(3929);
//        problemDto.setDK(3502);
//        problemDto.setEpsC(8);
//        problemDto.setFDu(64);
//        problemDto.setGC(62);
//
//        SolutionDto solutionDto = Generate2.generateSolution(problemDto);
//
//        assertEquals(5529,solutionDto.getDCt());
////        assertEquals(314,solutionDto.getDeltaDCt());
////        assertEquals(5843,solutionDto.getDCi());
//
//        assertEquals(350,solutionDto.getDeCt());
////        assertEquals(null,solutionDto.getDeltaDeCt());
////        assertEquals(null,solutionDto.getDeCi());
////
////        assertEquals(null,solutionDto.getKY());
////        assertEquals(null,solutionDto.getShY());
////        assertEquals(null,solutionDto.getDeltaX());
////
////
////        assertEquals(null,solutionDto.getPs());
////        assertEquals(null,solutionDto.getOp());
////        assertEquals(null,solutionDto.getVD());
////
////        assertEquals(null,solutionDto.getFDuOp());
////        assertEquals(null,solutionDto.getGCOp());
//    }
}