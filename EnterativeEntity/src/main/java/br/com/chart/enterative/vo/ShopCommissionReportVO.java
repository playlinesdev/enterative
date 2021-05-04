package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShopCommissionReportVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String productName;
    @Getter @Setter private BigDecimal faceValue;
    @Getter @Setter private BigDecimal chartValue;
    @Getter @Setter private BigDecimal chartPercentage;
    @Getter @Setter private List<ShopCommissionReportLineVO> lines;
}
