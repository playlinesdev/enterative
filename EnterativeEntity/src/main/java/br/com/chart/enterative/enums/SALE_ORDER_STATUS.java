package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum SALE_ORDER_STATUS {
    PENDING("base.pending", 0),
    ACTIVATED("base.activated", 1),
    DENIED("base.denied", 2),
    PAID("base.paid", 3),
    AWAITING_PAYMENT("base.awaitingpayment", 4);

    @Getter private final String description;
    @Getter private final Integer sequence;

    SALE_ORDER_STATUS(String descricao, Integer sequence) {
        this.description = descricao;
        this.sequence = sequence;
    }

    public static List<SALE_ORDER_STATUS> ordered() {
        return Arrays.stream(SALE_ORDER_STATUS.values()).sorted(Comparator.comparing(SALE_ORDER_STATUS::getDescription)).collect(Collectors.toList());
    }
}
