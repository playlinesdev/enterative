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
public enum SMS_STATUS {
    IDLE("base.idle", 0),
    SENT("base.sent", 1),
    ERROR("base.error", 2);

    @Getter private final String description;
    @Getter private final Integer sequence;

    SMS_STATUS(final String description, final Integer sequence) {
        this.description = description;
        this.sequence = sequence;
    }

    public List<SMS_STATUS> ordered() {
        return Arrays.stream(SMS_STATUS.values()).sorted(Comparator.comparing(SMS_STATUS::getSequence)).collect(Collectors.toList());
    }
}
