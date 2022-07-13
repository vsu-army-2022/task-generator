package edu.vsu.siuo.domains.dto;


import edu.vsu.siuo.domains.enums.Direction;
import lombok.Data;

import java.util.List;

@Data
public class SolutionDto {
    private int dCt;
    private int deltaDCt;
    private int dCi;

    private int deCt;
    private int deltaDeCt;
    private int deCi;

    /**
     * Коэффициент удаления, dK / dCt
     */
    private double kY;

    /**
     * Шаг угломера
     */
    private int shY;


    private double deltaX;

    /**
     * Поправка на смещение - угол между линией наблюдения и линией цели
     */
    private int ps;

    private Direction op;
    private int vD;

    private int fDuOp;
    private int gCOp;

    private List<TaskCommand> commands;

    @Data
    public static class TaskCommand {
        private String description;
        private Integer pR;
        private Integer yR;
        private Integer de;
        private String observation;
    }
}
