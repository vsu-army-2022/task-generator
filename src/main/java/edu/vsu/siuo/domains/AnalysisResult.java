package edu.vsu.siuo.domains;


import edu.vsu.siuo.domains.enums.Direction;
import lombok.Data;

@Data
public class AnalysisResult {
    double ps;
    double dovTop;
    double dalTop;
    Direction opDirection;
    double celX;
    double celY;

    // переменные для сопряженки
    double alfaNp;
    double dpL;
    double dpR;
    double apL;
    double apR;
    double gamma;
    // todo change to enum
    Direction opDirSopryzh;
}
