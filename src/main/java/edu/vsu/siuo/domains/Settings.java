package edu.vsu.siuo.domains;

import lombok.Data;

@Data
public class Settings {
    int maxCountOfTasks = 100;
    String defaultPath = System.getProperty("user.home") + "\\Desktop";
}
