package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William
 */
public enum ACTIVATION_PROCESS {
    BHN("base.bhn"),
    EPAY("base.epay");
    
    @Getter private final String description;
    
    ACTIVATION_PROCESS(String description) {
        this.description = description;
    }
    
    public static List<ACTIVATION_PROCESS> ordered() {
        return Arrays.stream(ACTIVATION_PROCESS.values()).sorted(Comparator.comparing(ACTIVATION_PROCESS::getDescription)).collect(Collectors.toList());
    }
}
