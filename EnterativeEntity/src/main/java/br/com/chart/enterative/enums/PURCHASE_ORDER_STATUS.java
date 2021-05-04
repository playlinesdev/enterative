package br.com.chart.enterative.enums;

public enum PURCHASE_ORDER_STATUS {

    PENDING("base.pending"),
    ACTIVE("base.active"),
    DENIED("base.denied");

    private final String descricao;

    PURCHASE_ORDER_STATUS(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
