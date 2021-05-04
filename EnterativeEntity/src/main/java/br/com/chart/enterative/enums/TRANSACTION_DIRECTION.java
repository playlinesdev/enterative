package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum TRANSACTION_DIRECTION {
    DISPATCH("base.dispatch", 0),
    RETURN("base.return", 1);

    @Getter private final String description;
    @Getter private final Integer sequence;

    TRANSACTION_DIRECTION(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<TRANSACTION_DIRECTION> ordered() {
        return Arrays.stream(TRANSACTION_DIRECTION.values()).sorted(Comparator.comparing(TRANSACTION_DIRECTION::getSequence)).collect(Collectors.toList());
    }
}
