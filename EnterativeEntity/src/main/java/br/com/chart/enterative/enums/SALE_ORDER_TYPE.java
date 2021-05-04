package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum SALE_ORDER_TYPE {

    PERSONALLY("base.personally", 0),
    VIRTUAL("base.virtual", 1);

    @Getter private final String description;
    @Getter private final Integer sequence;

    SALE_ORDER_TYPE(String description, Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<SALE_ORDER_TYPE> ordered() {
        return Arrays.stream(SALE_ORDER_TYPE.values()).sorted(Comparator.comparing(SALE_ORDER_TYPE::getSequence)).collect(Collectors.toList());
    }

}
