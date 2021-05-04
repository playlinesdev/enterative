package br.com.chart.enterative.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum FILE_TYPE {
    PRODUCT_IMAGE("base.productimage", 0);

    @Getter private final String description;
    @Getter private final Integer sequence;

    FILE_TYPE(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<FILE_TYPE> ordered() {
        return Arrays.stream(FILE_TYPE.values()).sorted(Comparator.comparing(FILE_TYPE::getSequence)).collect(Collectors.toList());
    }
}
