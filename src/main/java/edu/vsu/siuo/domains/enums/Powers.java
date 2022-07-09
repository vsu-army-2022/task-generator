package edu.vsu.siuo.domains.enums;

public enum Powers {
    Full("П"),
    Power1("1"),
    Power2("2"),
    Power3("3"),
    Power4("4"),
    Reduced("У");

    private final String description;

    Powers(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
