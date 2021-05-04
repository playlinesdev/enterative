package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PurchaseOrderVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private AccountVO account;
    @Getter @Setter private ShopVO shop;
    @Getter @Setter private BigDecimal totalAmount;
    @Getter @Setter private PURCHASE_ORDER_STATUS status;
    @Getter @Setter private UserVO activatedUser;
    @Getter @Setter private Date activatedDate;
    @Getter @Setter private List<PurchaseOrderLineVO> lines;
    @Getter @Setter private PurchaseOrderLineVO newLine;
    //UI
    @Getter @Setter private List<AccountTransactionVO> accountTransactions;
}
