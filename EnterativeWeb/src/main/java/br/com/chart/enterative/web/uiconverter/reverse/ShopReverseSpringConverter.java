package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ShopReverseSpringConverter extends BaseReverseSpringConverter<ShopVO> {

    @Override
    protected Supplier<ShopVO> getSupplier() {
        return ShopVO::new;
    }

}
