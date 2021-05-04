package br.com.chart.enterative.vo;

import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class VirtualActivationEmailVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String pin;
    @Getter @Setter private String cardNumber;
    @Getter @Setter private ProductVO product;
    @Getter @Setter private String terms;
    @Getter @Setter private String redemption;
    @Getter @Setter private Long saleOrderLineId;
}
