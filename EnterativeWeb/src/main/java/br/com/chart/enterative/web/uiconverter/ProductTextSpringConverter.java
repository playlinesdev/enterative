package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductTextSpringConverter extends BaseSpringConverter<ProductTextVO> {

    @Override
    protected Supplier<ProductTextVO> getSupplier() {
        return ProductTextVO::new;
    }
}
