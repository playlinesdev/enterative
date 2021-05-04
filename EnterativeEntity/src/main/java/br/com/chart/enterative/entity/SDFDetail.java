package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "sdf_detail")
public class SDFDetail extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "sdf_file_id")
    @Getter @Setter private SDFFile file;

    @Column(name = "merchant_id", length = 25)
    @Size(max = 25, message = ENTITY_MESSAGE.SIZE_25)
    @Getter @Setter private String merchantId;

    @Column(name = "merchant_name", length = 50)
    @Size(max = 50, message = ENTITY_MESSAGE.SIZE_50)
    @Getter @Setter private String merchantName;

    @Column(name = "store_id", length = 15)
    @Size(max = 15, message = ENTITY_MESSAGE.SIZE_15)
    @Getter @Setter private String storeId;

    @Column(name = "terminal_id", length = 16)
    @Size(max = 16, message = ENTITY_MESSAGE.SIZE_16)
    @Getter @Setter private String terminalId;

    @Column(name = "clerk_id", length = 20)
    @Size(max = 20, message = ENTITY_MESSAGE.SIZE_20)
    @Getter @Setter private String clerkId;

    @Column(name = "card_issuer_id", length = 25)
    @Size(max = 25, message = ENTITY_MESSAGE.SIZE_25)
    @Getter @Setter private String cardIssuerId;

    @Column(name = "card_issuer_processor_id", length = 25)
    @Size(max = 25, message = ENTITY_MESSAGE.SIZE_25)
    @Getter @Setter private String cardIssuerProcessorId;

    @Column(name = "acquirer_id")
    @Size(max = 25, message = ENTITY_MESSAGE.SIZE_25)
    @Getter @Setter private String acquirerId;

    @Temporal(TemporalType.DATE)
    @Column(name = "acquired_transaction_date")
    @Getter @Setter private Date acquiredTransactionDate;

    @Temporal(TemporalType.TIME)
    @Column(name = "acquired_transaction_time")
    @Getter @Setter private Date acquiredTransactionTime;

    @Column(name = "gift_card_number", length = 32)
    @Size(max = 32, message = ENTITY_MESSAGE.SIZE_32)
    @Getter @Setter private String giftCardNumber;

    @Column(name = "product_id", length = 30)
    @Size(max = 30, message = ENTITY_MESSAGE.SIZE_30)
    @Getter @Setter private String productId;

    @Temporal(TemporalType.DATE)
    @Column(name = "pos_transaction_date")
    @Getter @Setter private Date posTransactionDate;

    @Temporal(TemporalType.TIME)
    @Column(name = "pos_transaction_time")
    @Getter @Setter private Date posTransactionTime;

    @Column(name = "transaction_type", length = 8)
    @Size(max = 8, message = ENTITY_MESSAGE.SIZE_8)
    @Getter @Setter private String transactionType;

    @Column(name = "system_trace_audit_number", length = 16)
    @Size(max = 16, message = ENTITY_MESSAGE.SIZE_16)
    @Getter @Setter private String systemTraceAuditNumber;

    @Column(name = "product_item_price")
    @Getter @Setter private BigDecimal productItemPrice;

    @Column(name = "currency_code", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String currencyCode;

    @Column(name = "merchant_transaction_id", length = 16)
    @Size(max = 16, message = ENTITY_MESSAGE.SIZE_16)
    @Getter @Setter private String merchantTransactionId;

    @Column(name = "bhn_transaction_id", length = 30)
    @Size(max = 30, message = ENTITY_MESSAGE.SIZE_30)
    @Getter @Setter private String bhnTransactionId;

    @Column(name = "auth_response_code", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String authResponseCode;

    @Column(name = "approval_code", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String approvalCode;

    @Column(name = "reversal_type_code", length = 1)
    @Size(max = 1, message = ENTITY_MESSAGE.SIZE_1)
    @Getter @Setter private String reversalTypeCode;

    @Column(name = "transaction_amount")
    @Getter @Setter private BigDecimal transactionAmount;

    @Column(name = "consumer_fee_amount")
    @Getter @Setter private BigDecimal consumerFeeAmount;

    @Column(name = "commission_amount")
    @Getter @Setter private BigDecimal commissionAmount;

    @Column(name = "total_tax_transaction_amount")
    @Getter @Setter private BigDecimal totalTaxTransactionAmount;

    @Column(name = "total_tax_commission_amount")
    @Getter @Setter private BigDecimal totalTaxCommissionAmount;

    @Column(name = "net_amount")
    @Getter @Setter private BigDecimal netAmount;

    @Column(name = "transaction_id")
    @Getter @Setter private Long bhnTransaction;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private SDF_DETAIL_STATUS status;
}
