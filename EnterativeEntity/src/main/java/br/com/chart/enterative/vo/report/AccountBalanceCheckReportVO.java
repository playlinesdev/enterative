package br.com.chart.enterative.vo.report;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class AccountBalanceCheckReportVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private BigDecimal deposit;
    private BigDecimal approvedSale;
    private BigDecimal pendingSale;
    private BigDecimal commission;
    private BigDecimal calculatedBalance;
    private BigDecimal refund;
    private BigDecimal balance;
    private boolean isDifferent;

    public AccountBalanceCheckReportVO() {
        this.deposit = BigDecimal.ZERO;
        this.approvedSale = BigDecimal.ZERO;
        this.pendingSale = BigDecimal.ZERO;
        this.commission = BigDecimal.ZERO;
        this.calculatedBalance = BigDecimal.ZERO;
        this.refund = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
    }
}