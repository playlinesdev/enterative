package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFFileSummaryDiffVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long count;
    @Getter @Setter private BigDecimal transactionAmount;
    @Getter @Setter private BigDecimal consumerFeeAmount;
    @Getter @Setter private BigDecimal commissionAmount;
    @Getter @Setter private BigDecimal taxTransactionAmount;
    @Getter @Setter private BigDecimal taxCommissionAmount;
    @Getter @Setter private BigDecimal netAmount;
}
