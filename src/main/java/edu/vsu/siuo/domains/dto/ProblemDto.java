package edu.vsu.siuo.domains.dto;

import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import lombok.Data;

import java.util.List;

/**
 * Сгенерированное задание
 */
@Data
public class ProblemDto {
    private int on;
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

    private Targets targetType;
    private int alphaC;
    private int dK;
    private int epsC;
    private int fDu;
    private long gC;
}
