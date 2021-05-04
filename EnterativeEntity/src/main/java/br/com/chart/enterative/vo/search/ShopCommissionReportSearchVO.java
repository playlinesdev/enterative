package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShopCommissionReportSearchVO extends BaseVO {
    
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private ProductVO product;
    @Getter @Setter private ShopVO shop;
}
