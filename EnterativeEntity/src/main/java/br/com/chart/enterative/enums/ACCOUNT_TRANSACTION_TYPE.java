package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum ACCOUNT_TRANSACTION_TYPE {
    CREDIT("transaction.type.credit", 0),
    DEBIT("transaction.type.debit", 1);

    @Getter private final String description;
    @Getter private final Integer sequence;

    private ACCOUNT_TRANSACTION_TYPE(String description, Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<ACCOUNT_TRANSACTION_TYPE> ordered() {
        return Arrays.asList(values()).stream().sorted((t1, t2) -> t1.getSequence().compareTo(t2.getSequence())).collect(Collectors.toList());
    }
}
