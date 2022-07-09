package edu.vsu.siuo.domains.dto;

import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
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
public class ConditionsDto {
    private OP op;
    private ObjectPosition knp;
    private Target target;

    // todo change to enum
    private Powers power;
    private List<Integer> distance;
    private List<Integer> range;
    private List<Integer> direction;

    public ConditionsDto(OP op, ObjectPosition knp, Target target){
        this.op = op;
        this.knp = knp;
        this.target = target;
    }
}
