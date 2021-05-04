package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class BHNActivationVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ACTIVATION_STATUS status;
    @Getter @Setter private ACTIVATION_TYPE type;
    @Getter @Setter private ACTIVATION_QUEUE_STATUS queueStatus;
    @Getter @Setter private CALLBACK_STATUS callbackStatus;
    @Getter @Setter private Date reversalSentDate;
    @Getter @Setter private String responseCode;
    @Getter @Setter private Integer ttlActivation;
    @Getter @Setter private Integer ttlReversal;
    @Getter @Setter private Integer priority;
    @Getter @Setter private String externalCode;
    @Getter @Setter private String shopCode;
    @Getter @Setter private String terminal;
    @Getter @Setter private String callbackurl;
    @Getter @Setter private MerchantVO merchant;
    @Getter @Setter private BHNVoucherVO voucher;
    
    // UI
    @Getter @Setter private SaleOrderVO saleOrder;
    @Getter @Setter private List<BHNTransactionVO> transactions;
    @Getter @Setter private List<AccountTransactionVO> accountTransactions;
}
