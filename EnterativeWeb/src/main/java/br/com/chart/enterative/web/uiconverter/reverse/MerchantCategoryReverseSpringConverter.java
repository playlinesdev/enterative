package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class MerchantCategoryReverseSpringConverter extends BaseReverseSpringConverter<MerchantCategoryVO> {

    @Override
    protected Supplier<MerchantCategoryVO> getSupplier() {
        return MerchantCategoryVO::new;
    }

}
