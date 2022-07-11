package edu.vsu.siuo.domains.dto;

import edu.vsu.siuo.domains.enums.Types;
import lombok.Data;

@Data
public class ShotDto {
    Types type;
    int a;
    Integer f;

    public ShotDto(Types type, int a, Integer f) {
        this.a = a;
        this.type = type;
        this.f = f;
    }

    public ShotDto(Types type, int a) {
        this.a = a;
        this.type = type;
    }

    public ShotDto() {
    }
}
