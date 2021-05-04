package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum ACCOUNT_TRANSACTION_STATUS {
    PENDING("P", "base.pending", 1),
    ACTIVE("A", "base.active", 2),
    CANCELED("C", "base.canceled", 0);

    @Getter private final String code;
    @Getter private final String description;
    @Getter private final Integer sequence;

    private ACCOUNT_TRANSACTION_STATUS(String code, String description, Integer sequence) {
        this.code = code;
        this.description = description;
        this.sequence = sequence;
    }

    public static List<ACCOUNT_TRANSACTION_STATUS> ordered() {
        return Arrays.asList(values()).stream().sorted((t1, t2) -> t1.getSequence().compareTo(t2.getSequence())).collect(Collectors.toList());
    }
}
