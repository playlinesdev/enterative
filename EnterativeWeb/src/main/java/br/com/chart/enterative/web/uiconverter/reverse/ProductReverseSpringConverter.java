package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ProductReverseSpringConverter extends BaseReverseSpringConverter<ProductVO> {

    @Override
    protected Supplier<ProductVO> getSupplier() {
        return ProductVO::new;
    }

}
