package edu.vsu.siuo.domains.dto;

import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Targets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сгенерированное задание
 */
@Data
@NoArgsConstructor
public class ProblemDto {
    private OP op;
    private ObjectPosition knp;

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

    public ProblemDto(OP op, ObjectPosition knp, Powers load, List<Integer> distance, List<Integer> range, List<Integer> direction, Targets targetType,
                      int alphaC, int dk, int epsC, int fDu, long gC){
        this.op = op;
        this.knp = knp;
        this.load = load;
        this.distance = distance;
        this.range = range;
        this.direction = direction;
        this.targetType = targetType;
        this.alphaC = alphaC;
        this.dK = dk;
        this.epsC = epsC;
        this.fDu = fDu;
        this.gC = gC;
    }

    public ProblemDto(OP op, ObjectPosition knp){
        this.op = op;
        this.knp = knp;
    }
}
