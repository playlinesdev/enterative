package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFDetailVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private SDFFileVO file;
    @Getter @Setter private String merchantId;
    @Getter @Setter private String merchantName;
    @Getter @Setter private String storeId;
    @Getter @Setter private String terminalId;
    @Getter @Setter private String clerkId;
    @Getter @Setter private String cardIssuerId;
    @Getter @Setter private String cardIssuerProcessorId;
    @Getter @Setter private String acquirerId;
    @Getter @Setter private Date acquiredTransactionDate;
    @Getter @Setter private Date acquiredTransactionTime;
    @Getter @Setter private String giftCardNumber;
    @Getter @Setter private String productId;
    @Getter @Setter private Date posTransactionDate;
    @Getter @Setter private Date posTransactionTime;
    @Getter @Setter private String transactionType;
    @Getter @Setter private String systemTraceAuditNumber;
    @Getter @Setter private BigDecimal productItemPrice;
    @Getter @Setter private String currencyCode;
    @Getter @Setter private String merchantTransactionId;
    @Getter @Setter private String bhnTransactionId;
    @Getter @Setter private String authResponseCode;
    @Getter @Setter private String approvalCode;
    @Getter @Setter private String reversalTypeCode;
    @Getter @Setter private BigDecimal transactionAmount;
    @Getter @Setter private BigDecimal consumerFeeAmount;
    @Getter @Setter private BigDecimal commissionAmount;
    @Getter @Setter private BigDecimal totalTaxTransactionAmount;
    @Getter @Setter private BigDecimal totalTaxCommissionAmount;
    @Getter @Setter private BigDecimal netAmount;
    @Getter @Setter private BHNTransactionVO bhnTransaction;
    @Getter @Setter private SDF_DETAIL_STATUS status;
}
