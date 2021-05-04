package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum ACTIVATION_STATUS {
    ACTIVATION("base.activation"),
    REVERSAL("base.reversal"),
    PROCESSED("base.processed"),
    NONPROCESSED("base.nonprocessed"),
    CANCELED("base.canceled"),
    INCOMPLETE("base.incomplete");

    private final String description;

    ACTIVATION_STATUS(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public static List<ACTIVATION_STATUS> ordered() {
        return Arrays.stream(ACTIVATION_STATUS.values()).sorted(Comparator.comparing(ACTIVATION_STATUS::getDescription)).collect(Collectors.toList());
    }
}