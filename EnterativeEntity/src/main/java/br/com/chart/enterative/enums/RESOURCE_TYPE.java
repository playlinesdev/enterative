package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum RESOURCE_TYPE {
    FINANCIAL("base.financial", 0),
    PRE_AUTHORIZATION("base.preauthorization", 1),
    SAF("base.saf", 2),
    PRE_AUTOHRIZATION_SAF("base.preauthorizationsaf", 2),
    REVERSAL("base.reversal", 3),
    PRE_AUTOHRIZATION_REVERSAL("base.preauthorizationreversal", 4),
    NETWORK("base.network", 5);

    @Getter private final String description;
    @Getter private final Integer sequence;

    RESOURCE_TYPE(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public static List<RESOURCE_TYPE> ordered() {
        return Arrays.stream(RESOURCE_TYPE.values()).sorted(Comparator.comparing(RESOURCE_TYPE::getSequence)).collect(Collectors.toList());
    }
}
