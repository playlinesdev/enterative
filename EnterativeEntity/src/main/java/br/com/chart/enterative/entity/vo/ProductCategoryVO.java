package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ProductCategoryVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String code;
    @Getter @Setter private String name;
    @Getter @Setter private String displayName;
    @Getter @Setter private ProductCategoryVO parent;
    @Getter @Setter private List<ProductCategoryVO> children;
}
