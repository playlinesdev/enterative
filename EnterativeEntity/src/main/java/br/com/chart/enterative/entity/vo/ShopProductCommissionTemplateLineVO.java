package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 * @author William Leite
 */
public class ShopProductCommissionTemplateLineVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ProductVO product;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private BigDecimal percentage;

    public ShopProductCommissionTemplateLineVO() {
    }

    public ShopProductCommissionTemplateLineVO(ProductVO product, BigDecimal amount, BigDecimal percentage) {
        this.product = product;
        this.amount = amount;
        this.percentage = percentage;
    }
}
