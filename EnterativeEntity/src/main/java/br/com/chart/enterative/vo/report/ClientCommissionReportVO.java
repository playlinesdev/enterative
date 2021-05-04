package br.com.chart.enterative.vo.report;

import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ClientCommissionReportVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String order;
    @Getter @Setter private Date date;
    @Getter @Setter private String name;
    @Getter @Setter private BigDecimal value;
    @Getter @Setter private BigDecimal commission;
}
