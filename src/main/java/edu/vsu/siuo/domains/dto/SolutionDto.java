package edu.vsu.siuo.domains.dto;


import edu.vsu.siuo.domains.enums.Direction;
import lombok.Data;

import java.util.List;

/**
 * Решение задачи
 */
@Data
public class SolutionDto {
    private int dCt;
    private int deltaDCt;
    private int dCi;

    private int deCt;
    private int deltaDeCt;
    private int deCi;

    private int kY;
    private int shY;
    private int deltaX;

    private int ps;
    private Direction op;
    private int vD;

    private int fDuOp;
    private int gCOp;

    private List<TaskCommand> commands;

    @Data
    public static class TaskCommand {
        private String description;
        private int pR;
        private int yR;
        private String de;
        private String observation;
    }
}
