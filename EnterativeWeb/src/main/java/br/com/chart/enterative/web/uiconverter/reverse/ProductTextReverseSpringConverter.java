package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductTextReverseSpringConverter extends BaseReverseSpringConverter<ProductTextVO> {

    @Override
    protected Supplier<ProductTextVO> getSupplier() {
        return ProductTextVO::new;
    }

}
