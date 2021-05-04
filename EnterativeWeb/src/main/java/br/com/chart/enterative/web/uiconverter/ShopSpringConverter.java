package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ShopSpringConverter extends BaseSpringConverter<ShopVO> {

    @Override
    protected Supplier<ShopVO> getSupplier() {
        return ShopVO::new;
    }

}
