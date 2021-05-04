package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShopCommissionReportLineVO extends BaseVO {
    
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String shopName;
    @Getter @Setter private BigDecimal shopValue;
    @Getter @Setter private BigDecimal shopPercentage;
}
