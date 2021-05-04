package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PartnerVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String name;
    @Getter @Setter private List<ProductVO> products;
}
