package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFTrailerVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Date fileTransmissionDate;
    @Getter @Setter private Long totalActivationCount;
    @Getter @Setter private Long totalRedemptionCount;
    @Getter @Setter private Long totalReloadCount;
    @Getter @Setter private Long totalRefundActivationCount;
    @Getter @Setter private Long totalReturnCount;
    @Getter @Setter private Long totalReversalCount;
    @Getter @Setter private Long totalTransactionCount;
    @Getter @Setter private BigDecimal totalActivationAmount;
    @Getter @Setter private BigDecimal totalRedemptionAmount;
    @Getter @Setter private BigDecimal totalReloadAmount;
    @Getter @Setter private BigDecimal totalRefundActivationAmount;
    @Getter @Setter private BigDecimal totalReturnAmount;
    @Getter @Setter private BigDecimal totalReversalAmount;
    @Getter @Setter private BigDecimal totalConsumerFeeAmount;
    @Getter @Setter private BigDecimal totalCommissionAmount;
    @Getter @Setter private BigDecimal totalTaxTransactionAmount;
    @Getter @Setter private BigDecimal totalTaxCommissionAmount;
    @Getter @Setter private BigDecimal netTransactionAmount;
    @Getter @Setter private String filler;
}
