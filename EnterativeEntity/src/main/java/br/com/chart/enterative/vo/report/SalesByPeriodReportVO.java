package br.com.chart.enterative.vo.report;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author William Leite
 */
@Getter
@Setter
public class SalesByPeriodReportVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String name;
    private String state;
    private String city;
    private String district;
    private BigDecimal amount;
}