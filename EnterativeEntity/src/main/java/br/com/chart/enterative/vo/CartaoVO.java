package br.com.chart.enterative.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class CartaoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String cardNo;
    @Getter @Setter private String barcode;
    @Getter @Setter private String ean;
    @Getter @Setter private String produto;
    @Getter @Setter private String produtoImagem;
    @Getter @Setter private String numeroPedido;
    @Getter @Setter private String statusPedido;
    @Getter @Setter private String resposta;
    @Getter @Setter private String respostaAux;
    @Getter @Setter private BigDecimal valor;
    // Fluxo Enterative Balance
    @Getter @Setter private BigDecimal shopBalance;
    // Error Handling
    @Getter @Setter private String errorMessage;
    @Getter @Setter private String retryAction;

    public CartaoVO() {
    }

    public CartaoVO(String barcode) {
        this.barcode = barcode;
    }
}
