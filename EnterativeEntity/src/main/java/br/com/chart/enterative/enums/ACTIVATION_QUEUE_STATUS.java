package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum ACTIVATION_QUEUE_STATUS {
    IDLE("base.idle", 0),
    QUEUED("base.queued", 1),
    PROCESSED("base.processed", 2);

    @Getter private final String description;
    @Getter private final Integer sequence;

    ACTIVATION_QUEUE_STATUS(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public List<ACTIVATION_QUEUE_STATUS> ordered() {
        return Arrays.stream(ACTIVATION_QUEUE_STATUS.values()).sorted(Comparator.comparing(ACTIVATION_QUEUE_STATUS::getSequence)).collect(Collectors.toList());
    }
}
