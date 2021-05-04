package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class MerchantCategorySpringConverter extends BaseSpringConverter<MerchantCategoryVO> {

    @Override
    protected Supplier<MerchantCategoryVO> getSupplier() {
        return MerchantCategoryVO::new;
    }
}
