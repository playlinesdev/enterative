package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum EMAIL_STATUS {
    IDLE("base.idle", 0),
    SENT("base.sent", 1),
    ERROR("base.error", 2);

    @Getter private final String description;
    @Getter private final Integer sequence;

    EMAIL_STATUS(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public List<EMAIL_STATUS> ordered() {
        return Arrays.stream(EMAIL_STATUS.values()).sorted(Comparator.comparing(EMAIL_STATUS::getSequence)).collect(Collectors.toList());
    }
}
