package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ProductSearchVO extends NamedVO{

    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String ean;
    @Getter @Setter private PRODUCT_TYPE type;
    @Getter @Setter private STATUS status;
    @Getter @Setter private ProductCategoryVO category;
    @Getter @Setter private ACTIVATION_PROCESS activationProcess;
}
