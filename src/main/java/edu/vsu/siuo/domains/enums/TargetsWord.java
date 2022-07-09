package edu.vsu.siuo.domains.enums;

public enum TargetsWord {
    PO("открыто расположенные ЖС и ОС"),
    PU("ЖС и ОС, расположенные в окопах (траншеях)"),
    BATR("батарея"),
    VZV("взвод буксируемых орудий"),
    RAP("радиолокационная станция полевой артиллерии"),
    PTUR("птур в окопе");

    private final String description;

    TargetsWord(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
