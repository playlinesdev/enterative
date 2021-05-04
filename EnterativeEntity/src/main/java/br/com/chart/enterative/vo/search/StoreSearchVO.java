package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.vo.SortColumnVO;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class StoreSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ProductCategoryVO productCategory;
    @Getter @Setter private SortColumnVO sortColumn;
}
