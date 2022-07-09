package edu.vsu.siuo.domains.dto;

import lombok.Data;

@Data
public class TaskDto {
    private int taskNumber;
    private int taskTopic;

    ConditionsDto problemDto;
    SolutionDto solutionDto;
}
