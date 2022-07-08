package edu.vsu.siuo.domains;

import edu.vsu.siuo.domains.enums.Powers;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private int taskNumber;
    private int taskTopic;

    private int oh;
    private int xOp;
    private int yOp;
    private int hOp;
    private int xKnp;
    private int yKnp;
    private int hKnp;

    // todo change to enum
    private Powers load;
    private List<Integer> distance;
    private List<Integer> range;
    private List<Integer> direction;

    private String targetType;
    private int alphaC;
    private int dK;
    private int epsC;
    private int fDu;
    private long gC;

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
    private String op;
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
