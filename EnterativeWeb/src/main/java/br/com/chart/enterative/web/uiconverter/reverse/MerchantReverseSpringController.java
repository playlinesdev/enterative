package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class MerchantReverseSpringController extends BaseReverseSpringConverter<MerchantVO> {

    @Override
    protected Supplier<MerchantVO> getSupplier() {
        return MerchantVO::new;
    }

}
