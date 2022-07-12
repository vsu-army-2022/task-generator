package edu.vsu.siuo.domains.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Types {
    EMPTY(""),
    XZ("«?»"),
    ONE_P("«+»"),
    ONE_N("«-»"),
    ALL_P("Все «+»"),
    ALL_N("Все «-»"),
    PRE_P("Преобладание «+»"),
    PRE_N("Преобладание «-»"),
    RAV_P("Равенство «+» и «-» от-но ДГЦ"),
    RAV_N("Равенство «+» и «-» от-но БГЦ"),
    TARGET_DESTROYED("Цель подавлена");

    private final String description;

    Types(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<Types> getNoEmptyTypes() {
        return Arrays.stream(values())
                .filter(types -> !types.equals(EMPTY))
                .collect(Collectors.toList());
    }
}
