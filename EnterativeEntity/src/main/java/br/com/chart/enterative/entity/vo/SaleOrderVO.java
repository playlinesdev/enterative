package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import java.math.BigDecimal;
import java.util.List;

import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class SaleOrderVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    private AccountVO account;
    private ShopVO shop;
    private BigDecimal amount;
    private SALE_ORDER_STATUS status;
    private SALE_ORDER_TYPE type;
    private String customerMobile;
    private EMAIL_STATUS emailStatus;
    private List<SaleOrderLineVO> lines;

    // Flutter
    private String translatedStatus;
    private String translatedType;
}
