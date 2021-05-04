package br.com.chart.enterative.vo.report;

import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SalesByProductItemReportVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BigDecimal quantity;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private BigDecimal accountCommission;
    @Getter @Setter private BigDecimal chartCommission;
    @Getter @Setter private BigDecimal profit;

    public SalesByProductItemReportVO() {
        this.amount = BigDecimal.ZERO;
        this.chartCommission = BigDecimal.ZERO;
        this.profit = BigDecimal.ZERO;
        this.quantity = BigDecimal.ZERO;
        this.accountCommission = BigDecimal.ZERO;
    }
}
