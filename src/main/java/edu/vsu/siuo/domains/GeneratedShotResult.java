package edu.vsu.siuo.domains;

import edu.vsu.siuo.domains.dto.ShotDto;
import lombok.Data;

import java.util.Map;

// todo delete this class

@Data
public class GeneratedShotResult {
    private Map<Integer, ShotDto> shot;
    private String vse3v;
    private String word;

    public GeneratedShotResult(Map<Integer, ShotDto> shot, String vse3v, String word) {
        this.shot = shot;
        this.vse3v = vse3v;
        this.word = word;
    }

    public GeneratedShotResult(Map<Integer, ShotDto> shot) {
        this.shot = shot;
    }
}
