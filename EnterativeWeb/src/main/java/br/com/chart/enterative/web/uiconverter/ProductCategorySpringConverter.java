package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductCategorySpringConverter extends BaseSpringConverter<ProductCategoryVO> {

    @Override
    protected Supplier<ProductCategoryVO> getSupplier() {
        return ProductCategoryVO::new;
    }
}
