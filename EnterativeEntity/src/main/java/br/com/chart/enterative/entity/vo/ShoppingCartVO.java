package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShoppingCartVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private List<ShoppingCartLineVO> lines;

}
