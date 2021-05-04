package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ProductTextVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String description;
    @Getter @Setter private PRODUCT_TEXT_TYPE type;
}
