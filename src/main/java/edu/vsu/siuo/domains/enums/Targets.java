package edu.vsu.siuo.domains.enums;

import lombok.Data;


public enum Targets {
    PO("пехота"),
    PU("пехота укрытая"),
    BATR("батарея"),
    VZV("взвод артиллерийский"),
    RAP("РЛС"),
    PTUR("птур: в окопе"),
    ;

    private final String description;

    Targets(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
