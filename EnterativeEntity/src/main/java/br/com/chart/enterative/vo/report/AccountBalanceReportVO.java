package br.com.chart.enterative.vo.report;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class AccountBalanceReportVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String name;
    @Getter @Setter private BigDecimal balance;
}