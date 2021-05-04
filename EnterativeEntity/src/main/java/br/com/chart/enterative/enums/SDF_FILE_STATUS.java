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
public enum SDF_FILE_STATUS {
    PENDING("base.pending", 0),
    IN_PROGRESS("base.inprogress", 1),
    DONE("base.done", 2),
    ERROR("base.error", 3);

    @Getter private final String description;
    @Getter private final Integer sequence;

    SDF_FILE_STATUS(String desc, Integer sequence) {
        this.description = desc;
        this.sequence = sequence;
    }

    public static List<SDF_FILE_STATUS> ordered() {
        return Arrays.stream(SDF_FILE_STATUS.values()).sorted(Comparator.comparing(SDF_FILE_STATUS::getSequence)).collect(Collectors.toList());
    }
}
