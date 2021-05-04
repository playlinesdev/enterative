package br.com.chart.enterative.vo.report;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.vo.base.BaseVO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author William Leite
 */
@Getter
@Setter
public class SalesByProductReportVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private ProductVO product;
    private AccountVO account;
    private Map<String, SalesByProductItemReportVO> items;
    private BigDecimal quantity;
    private BigDecimal amount;
    private BigDecimal chartCommission;
    private BigDecimal accountCommission;
    private BigDecimal profit;
    private Map<String, SalesByProductPaymentReportVO> payments;

    public SalesByProductReportVO() {
        this.items = new HashMap<>(0);
        this.quantity = BigDecimal.ZERO;
        this.amount = BigDecimal.ZERO;
        this.chartCommission = BigDecimal.ZERO;
        this.accountCommission = BigDecimal.ZERO;
        this.profit = BigDecimal.ZERO;

        this.payments = new HashMap<>(0);
        this.payments.put("PAGSEGURO", new SalesByProductPaymentReportVO());
        this.payments.put("ENTERATIVEPAY", new SalesByProductPaymentReportVO());
        this.payments.put("CIELO", new SalesByProductPaymentReportVO());
    }

    public SalesByProductReportVO(ProductVO product) {
        this();
        this.product = product;
    }

    public SalesByProductReportVO(AccountVO account) {
        this();
        this.account = account;
    }
}