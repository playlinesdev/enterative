package br.com.chart.enterative.vo;

import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PersonallyActivationEmailVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long orderId;
    @Getter @Setter private SALE_ORDER_STATUS status;
    @Getter @Setter private ProductVO product;
}