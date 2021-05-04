package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum PAGSEGURO_PAYMENT_METHOD {
    EFT("EFT", "base.eft", 0),
    BOLETO("BOLETO", "base.boleto", 1),
    CREDIT_CARD("CREDIT_CARD", "base.creditcard", 2),
    BALANCE("BALANCE", "base.pagsegurobalance", 3),
    DEPOSIT("DEPOSIT", "base.deposit", 4);

    @Getter private final String description;
    @Getter private final Integer sequence;
    @Getter private final String code;
    

    PAGSEGURO_PAYMENT_METHOD(String code, String description, Integer sequence) {
        this.code = code;
        this.description = description;
        this.sequence = sequence;
    }

    public static List<PAGSEGURO_PAYMENT_METHOD> ordered() {
        return Arrays.stream(PAGSEGURO_PAYMENT_METHOD.values())
                .sorted((t1, t2) -> t1.getSequence().compareTo(t2.getSequence()))
                .collect(Collectors.toList());
    }
}
