package br.com.enterative.demo.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum ACTIVATION_TYPE {
    PHYSICAL("base.physical", 0),
    VIRTUAL("base.virtual", 1);

    @Getter private final String description;
    @Getter private final Integer sequence;

    ACTIVATION_TYPE(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public List<ACTIVATION_TYPE> ordered() {
        return Arrays.stream(ACTIVATION_TYPE.values()).sorted(Comparator.comparing(ACTIVATION_TYPE::getSequence)).collect(Collectors.toList());
    }
}
