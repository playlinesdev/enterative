package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductSpringConverter extends BaseSpringConverter<ProductVO> {

    @Override
    protected Supplier<ProductVO> getSupplier() {
        return ProductVO::new;
    }

}
