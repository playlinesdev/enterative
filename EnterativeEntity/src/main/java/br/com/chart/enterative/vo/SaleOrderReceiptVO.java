package br.com.chart.enterative.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SaleOrderReceiptVO implements Serializable {
    private Date date;
    private BigDecimal amount;
    private String status;
    private String type;
    private String product;
    private String pin;
}
