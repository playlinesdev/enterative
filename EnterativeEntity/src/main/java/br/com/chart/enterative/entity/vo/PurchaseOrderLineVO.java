package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PurchaseOrderLineVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter public String name;
    @Getter @Setter public BigDecimal amount;
    @Getter @Setter public PURCHASE_ORDER_STATUS status;
}
