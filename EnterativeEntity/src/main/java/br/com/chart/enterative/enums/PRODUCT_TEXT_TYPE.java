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
public enum PRODUCT_TEXT_TYPE {
    TERMS_AND_CONDITIONS("base.termsandconditions", 0),
    ACTIVATION_INSTRUCTIONS("base.activationinstructions", 1);
    
    @Getter private final String description;
    @Getter private final int sequence;
    
    PRODUCT_TEXT_TYPE(final String description, final int sequence) {
        this.description = description;
        this.sequence = sequence;
    }
    
    public static List<PRODUCT_TEXT_TYPE> ordered() {
        return Arrays.stream(PRODUCT_TEXT_TYPE.values()).sorted(Comparator.comparing(PRODUCT_TEXT_TYPE::getSequence)).collect(Collectors.toList());
    }
}
