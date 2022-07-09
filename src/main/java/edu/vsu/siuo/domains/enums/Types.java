package edu.vsu.siuo.domains.enums;

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
    RAV_N("Равенство «+» и «-» от-но БГЦ");

    private final String description;

    Types(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
