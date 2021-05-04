package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductCategoryReverseSpringConverter extends BaseReverseSpringConverter<ProductCategoryVO> {

    @Override
    protected Supplier<ProductCategoryVO> getSupplier() {
        return ProductCategoryVO::new;
    }

}
