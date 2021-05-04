package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShoppingCartLineVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ProductVO product;
    @Getter @Setter private BigDecimal quantity;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private BigDecimal totalAmount;
    @Getter @Setter private String storeStep;
    @Getter @Setter private String userEmail;
    @Getter @Setter private String userCellphone;
}
