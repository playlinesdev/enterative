package br.com.chart.enterative.vo;

import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFFileSummaryDetailVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long id;
    @Getter @Setter private SDF_DETAIL_STATUS status;
    @Getter @Setter private String transactionType;
    @Getter @Setter private String reversalTypeCode;
    @Getter @Setter private Long count;
    @Getter @Setter private BigDecimal transactionAmount;
    @Getter @Setter private BigDecimal consumerFeeAmount;
    @Getter @Setter private BigDecimal commissionAmount;
    @Getter @Setter private BigDecimal taxTransactionAmount;
    @Getter @Setter private BigDecimal taxCommissionAmount;
    @Getter @Setter private BigDecimal netAmount;

    public SDFFileSummaryDetailVO() {
        this.count = 0L;
        this.transactionAmount = BigDecimal.ZERO.setScale(2);
        this.consumerFeeAmount = BigDecimal.ZERO.setScale(2);
        this.commissionAmount = BigDecimal.ZERO.setScale(2);
        this.taxCommissionAmount = BigDecimal.ZERO.setScale(2);
        this.taxTransactionAmount = BigDecimal.ZERO.setScale(2);
        this.netAmount = BigDecimal.ZERO.setScale(2);
    }

    public SDFFileSummaryDetailVO(Long count, BigDecimal transactionAmount, BigDecimal consumerFeeAmount, BigDecimal commissionAmount, BigDecimal taxTransactionAmount, BigDecimal taxCommissionAmount, BigDecimal netAmount) {
        this.count = count;
        this.transactionAmount = transactionAmount;
        this.consumerFeeAmount = consumerFeeAmount;
        this.commissionAmount = commissionAmount;
        this.taxTransactionAmount = taxTransactionAmount;
        this.taxCommissionAmount = taxCommissionAmount;
        this.netAmount = netAmount;
    }
}
