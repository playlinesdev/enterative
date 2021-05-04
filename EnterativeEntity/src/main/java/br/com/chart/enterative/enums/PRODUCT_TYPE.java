package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum PRODUCT_TYPE {

    CARD("base.card", 0),
    VIRTUAL("base.virtual", 1);

    @Getter private final String description;
    @Getter private final Integer sequence;

    PRODUCT_TYPE(String description, Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<PRODUCT_TYPE> ordered() {
        return Arrays.stream(PRODUCT_TYPE.values()).sorted(Comparator.comparing(PRODUCT_TYPE::getSequence)).collect(Collectors.toList());
    }

}
