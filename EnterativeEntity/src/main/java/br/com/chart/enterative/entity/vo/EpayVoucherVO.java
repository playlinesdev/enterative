package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class EpayVoucherVO extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String ean;
    @Getter @Setter private ProductVO product;
    @Getter @Setter private String operator;
    @Getter @Setter private String areaCode;
    @Getter @Setter private String phone;
    @Getter @Setter private String epayProductId;
    @Getter @Setter private BigDecimal amount;
}