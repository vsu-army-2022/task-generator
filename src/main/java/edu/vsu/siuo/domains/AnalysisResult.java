package edu.vsu.siuo.domains;


import lombok.Data;

@Data
public class AnalysisResult {
    double ps;
    double alfaNp;
    double dovTop;
    double dalTop;
    String opDir;
    double dpL;
    double dpR;
    double apL;
    double apR;
    double gamma;
    // todo change to enum
    String opDirSopryzh;
    double celX;
    double celY;
}
