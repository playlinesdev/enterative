package br.com.chart.enterative.enums;

/**
 *
 * @author William Leite
 */
public enum CALLBACK_STATUS {
    PENDING("base.pending"),
    WAITING("base.waiting"),
    ERROR("base.error"),
    DONE("base.done");

    private final String description;

    CALLBACK_STATUS(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
