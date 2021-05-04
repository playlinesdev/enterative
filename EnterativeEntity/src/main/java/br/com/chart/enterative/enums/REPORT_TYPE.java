package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 */
public enum REPORT_TYPE {
    ANALYTICAL("base.analytical", 0),
    SYNTHETIC("base.synthetic", 1);

    private final String description;
    private final Integer sequence;

    private REPORT_TYPE(String description, Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public static List<REPORT_TYPE> ordered() {
        return Arrays.asList(values()).stream().sorted((t1, t2) -> t1.getSequence().compareTo(t2.getSequence())).collect(Collectors.toList());
    }
}
