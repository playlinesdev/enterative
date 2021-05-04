package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.STATUS;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author William
 */
public class ShopProductCommissionTemplateVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private List<ShopProductCommissionTemplateLineVO> lines;
}
