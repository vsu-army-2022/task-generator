package edu.vsu.siuo.domains;

import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private int OH;
    private int xop;
    private int yop;
    private int hop;
    private int xknp;
    private int yknp;
    private int hknp;

    // todo change to enum
    private String load;
    private List<Integer> distance;
    private List<Integer> range;
    private List<Integer> direction;

    private String targetType;
    private int ak;
    private int dk;
}
