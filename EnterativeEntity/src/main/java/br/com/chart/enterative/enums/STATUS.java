package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum STATUS {
    ACTIVE("A", "base.active", 0),
    INACTIVE("I", "base.inactive", 1),
    CANCELED("C", "base.canceled", 2);

    @Getter private final String code;
    @Getter private final String description;
    @Getter private final Integer sequence;

    private STATUS(String code, String description, Integer sequence) {
        this.code = code;
        this.description = description;
        this.sequence = sequence;
    }

    public static List<STATUS> ordered() {
        return Arrays.stream(STATUS.values()).sorted(Comparator.comparing(STATUS::getSequence)).collect(Collectors.toList());
    }
}
