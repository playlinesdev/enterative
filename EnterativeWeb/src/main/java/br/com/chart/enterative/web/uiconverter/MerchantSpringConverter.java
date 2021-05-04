package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class MerchantSpringConverter extends BaseSpringConverter<MerchantVO> {

    @Override
    protected Supplier<MerchantVO> getSupplier() {
        return MerchantVO::new;
    }
}
