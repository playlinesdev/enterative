package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class AccountTransactionVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private ACCOUNT_TRANSACTION_TYPE type;
    @Getter @Setter private ACCOUNT_TRANSACTION_STATUS status;
    @Getter @Setter private Date transactionDate;
    @Getter @Setter private SaleOrderLineVO saleOrderLine;
    @Getter @Setter private PurchaseOrderLineVO purchaseOrderLine;
    @Getter @Setter private AccountVO account;
    @Getter @Setter private AccountTransactionCategoryVO category;
    @Getter @Setter private Long productId;
    @Getter @Setter private String productName;
    @Getter @Setter private BigDecimal currentValue;
    // UI
    @Getter @Setter private Long saleOrderNumber;
    @Getter @Setter private Long purchaseOrderNumber;
    @Getter @Setter private BigDecimal commissionAmount;
    @Getter @Setter private BigDecimal commissionPercentage;    
}
