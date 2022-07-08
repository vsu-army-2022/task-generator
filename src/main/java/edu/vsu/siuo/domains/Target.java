package edu.vsu.siuo.domains;

import edu.vsu.siuo.domains.enums.Targets;
import lombok.Data;

@Data
public class Target extends ObjectPosition{
    private int targetsDepth;
    private int targetsFrontDu;
    private Targets type;
}
