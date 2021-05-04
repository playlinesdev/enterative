package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SHOP_PHONE_TYPE {
    COMMERCIAL("base.commercial", 0),
    RESIDENTIAL("base.residential", 1),
    MOBILE("base.mobile", 2);

    private final String description;
    private final Integer sequence;

    private SHOP_PHONE_TYPE(String description, Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public static List<SHOP_PHONE_TYPE> ordered() {
        return Arrays.asList(values()).stream().sorted((t1, t2) -> t1.getSequence().compareTo(t2.getSequence())).collect(Collectors.toList());
    }
}
