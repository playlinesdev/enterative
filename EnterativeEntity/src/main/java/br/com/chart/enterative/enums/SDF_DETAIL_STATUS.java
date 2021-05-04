package br.com.chart.enterative.enums;

/**
 *
 * @author William Leite
 */
public enum SDF_DETAIL_STATUS {
    NOT_FOUND("base.notfound"),
    FOUND("base.found"),
    FOUND_WRONG("base.foundmanually");

    private String desc;

    private SDF_DETAIL_STATUS(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
