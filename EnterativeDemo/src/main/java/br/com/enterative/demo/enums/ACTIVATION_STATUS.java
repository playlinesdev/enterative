package br.com.enterative.demo.enums;

public enum ACTIVATION_STATUS {
    ACTIVATION("base.activation"),
    REVERSAL("base.reversal"),
    PROCESSED("base.processed"),
    NONPROCESSED("base.nonprocessed");

    private final String description;

    ACTIVATION_STATUS(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
