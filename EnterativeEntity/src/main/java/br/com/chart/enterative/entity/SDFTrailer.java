package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "sdf_trailer")
public class SDFTrailer extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "sdf_file_id")
    @Getter @Setter private SDFFile file;

    @Temporal(TemporalType.DATE)
    @Column(name = "file_transmission_date")
    @Getter @Setter private Date fileTransmissionDate;

    @Column(name = "total_activation_count")
    @Getter @Setter private Long totalActivationCount;

    @Column(name = "total_redemption_count")
    @Getter @Setter private Long totalRedemptionCount;

    @Column(name = "total_reload_count")
    @Getter @Setter private Long totalReloadCount;

    @Column(name = "total_refund_activation_count")
    @Getter @Setter private Long totalRefundActivationCount;

    @Column(name = "total_return_count")
    @Getter @Setter private Long totalReturnCount;

    @Column(name = "total_reversal_count")
    @Getter @Setter private Long totalReversalCount;

    @Column(name = "total_transaction_count")
    @Getter @Setter private Long totalTransactionCount;

    @Column(name = "total_activation_amount")
    @Getter @Setter private BigDecimal totalActivationAmount;

    @Column(name = "total_redemption_amount")
    @Getter @Setter private BigDecimal totalRedemptionAmount;

    @Column(name = "total_reload_amount")
    @Getter @Setter private BigDecimal totalReloadAmount;

    @Column(name = "total_refund_activation_amount")
    @Getter @Setter private BigDecimal totalRefundActivationAmount;

    @Column(name = "total_return_amount")
    @Getter @Setter private BigDecimal totalReturnAmount;

    @Column(name = "total_reversal_amount")
    @Getter @Setter private BigDecimal totalReversalAmount;

    @Column(name = "total_consumer_fee_amount")
    @Getter @Setter private BigDecimal totalConsumerFeeAmount;

    @Column(name = "total_commission_amount")
    @Getter @Setter private BigDecimal totalCommissionAmount;

    @Column(name = "total_tax_transaction_amount")
    @Getter @Setter private BigDecimal totalTaxTransactionAmount;

    @Column(name = "total_tax_commission_amount")
    @Getter @Setter private BigDecimal totalTaxCommissionAmount;

    @Column(name = "net_transaction_amount")
    @Getter @Setter private BigDecimal netTransactionAmount;

    @Column(name = "filler", length = 130)
    @Size(max = 130, message = ENTITY_MESSAGE.SIZE_130)
    @Getter @Setter private String filler;
}
