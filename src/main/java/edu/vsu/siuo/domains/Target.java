package edu.vsu.siuo.domains;

import edu.vsu.siuo.domains.enums.Targets;
import lombok.Data;

@Data
public class Target extends ObjectPosition{
    private double targetsDepth;
    private double targetsFrontDu;
    private double targetsDepthOP;
    private double targetsFrontDuOP;
    private int distanceFromKNPtoTarget;
    private int angleFromKNPtoTarget;
    private int angularMagnitudeTarget;
    private Targets type;
}

